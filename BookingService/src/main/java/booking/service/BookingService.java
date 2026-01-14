package booking.service;

import booking.dao.Booking;
import booking.dto.CreateBookingRequestDTO;
import booking.dto.RoomResponseDTO;
import booking.exceptions.ResourceNotFoundException;
import booking.mapping.BookingMapper;
import booking.repository.BookingRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLOutput;
import java.util.Date;
import java.util.List;

@Service
public class BookingService {
    private final BookingAsyncService bookingAsyncService;
    private final BookingRepository bookingRepository;
    private final HotelService hotelService;
    private static final Logger log = LoggerFactory.getLogger(BookingService.class);

    public BookingService(BookingRepository bookingRepository, BookingAsyncService bookingAsyncService, HotelService hotelService) {
        this.bookingRepository = bookingRepository;
        this.bookingAsyncService = bookingAsyncService;
        this.hotelService = hotelService;
    }

    public Booking getBookingById(Long id)  {
        return bookingRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Booking not found: " + id));
    }

    public List<Booking> getBookingsByUserId(Long userId) {
        return bookingRepository.findByUserId(userId);
    }

    public Booking createBooking(CreateBookingRequestDTO dto, Long userId) {

        Booking booking = BookingMapper.INSTANCE.toEntity(dto, userId);

        log.info("Booking creating: {} for user: {} for date: {} to: {}",
                booking, userId, booking.getStartDate(), booking.getEndDate());

        if (dto.autoSelect) {
            List<RoomResponseDTO> rooms = hotelService.getAvailableRooms();
            if (rooms == null || rooms.isEmpty()) {
                throw new ResourceNotFoundException("No available rooms", "room_id", booking.getRoomId());
            }

            RoomResponseDTO availableRoom = findAFreeRoom(rooms,
                    booking.getStartDate(), booking.getEndDate());
            if (availableRoom == null) {
                throw new ResourceNotFoundException("No available rooms", "room_id", booking.getRoomId());
            }

            booking.setRoomId(availableRoom.id);
        } else if (dto.roomId != null) {
            boolean roomIsBusy = bookingRepository.roomIsBusy(booking.getRoomId(), booking.getStartDate(), booking.getEndDate());
            if (roomIsBusy) {
                return null;
            }
        } else {
            return null;
        }

        bookingRepository.save(booking);

        bookingAsyncService.updateBookingStatusAsync(booking.getId(), booking.getRoomId());

        return booking;
    }

    public void updateBooking(Booking booking) {
        bookingRepository.save(booking);
    }

    public void deleteBooking(Long id) {
        bookingRepository.deleteById(id);
    }

    private RoomResponseDTO findAFreeRoom(List<RoomResponseDTO> rooms, Date startDate, Date endDate) {
        for (RoomResponseDTO room : rooms) {
            boolean roomIsBusy = bookingRepository.roomIsBusy(room.id, startDate, endDate);
            if (!roomIsBusy) {
                return room;
            }
        }
        return null;
    }
}
