package booking.service;

import booking.dao.Booking;
import booking.dto.CreateBookingRequestDTO;
import booking.dto.RoomResponseDTO;
import booking.mapping.BookingMapper;
import booking.repository.BookingRepository;
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
    public BookingService(BookingRepository bookingRepository, BookingAsyncService bookingAsyncService, HotelService hotelService) {
        this.bookingRepository = bookingRepository;
        this.bookingAsyncService = bookingAsyncService;
        this.hotelService = hotelService;
    }

    public Booking getBookingById(Long id)  {
        return bookingRepository.findById(id).orElse(null);
    }

    public List<Booking> getBookingsByUserId(Long userId) {
        return bookingRepository.findByUserId(userId);
    }

    public Booking createBooking(CreateBookingRequestDTO dto, Long userId) {

        Booking booking = BookingMapper.INSTANCE.toEntity(dto, userId);


        System.out.println("Booking creating: " + booking.getRoomId()
        + " userId: " + userId
        + " startDate: " + booking.getStartDate()
        );

        if (dto.autoSelect) {
            List<RoomResponseDTO> rooms = hotelService.getAvailableRooms();

            RoomResponseDTO availableRoom = findAFreeRoom(rooms,
                    booking.getStartDate(), booking.getEndDate());
            if (availableRoom == null) {
                return null;
            }

            booking.setRoomId(availableRoom.roomId);
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
            boolean roomIsBusy = bookingRepository.roomIsBusy(room.roomId, startDate, endDate);
            if (!roomIsBusy) {
                return room;
            }
        }
        return null;
    }
}
