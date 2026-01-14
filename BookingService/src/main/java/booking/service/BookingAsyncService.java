package booking.service;

import booking.dao.Booking;
import booking.repository.BookingRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class BookingAsyncService {

    private final HotelService hotelService;
    private final BookingRepository bookingRepository;

    public BookingAsyncService(HotelService hotelService, BookingRepository bookingRepository) {
        this.hotelService = hotelService;
        this.bookingRepository = bookingRepository;
    }


    @Async
    public CompletableFuture<Void> updateBookingStatusAsync(Long bookingId, Long roomId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found: " + bookingId));
        boolean booked = false;
        try {
            boolean available = hotelService.checkAvailability(roomId);
            booking.setStatus(available ? "CONFIRMED" : "CANCELLED");
            bookingRepository.save(booking);
            booked = true;
        } catch (Exception e) {
            booking.setStatus("CANCELLED");
            bookingRepository.save(booking);
        } finally {
            hotelService.release(roomId, booked);
        }

        return CompletableFuture.completedFuture(null);
    }
}
