package booking.service;

import booking.dao.Booking;
import booking.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class BookingAsyncService {

    private HotelService hotelService;
    private BookingRepository bookingRepository;

    @Async
    public CompletableFuture<Void> updateBookingStatusAsync(Long bookingId, Long roomId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
        try {
            boolean available = hotelService.checkAvailability(roomId);
            if (available) {
                booking.setStatus("CONFIRMED");
            } else {
                booking.setStatus("CANCELLED");
            }
        } catch (Exception e) {
            booking.setStatus("CANCELLED");
        } finally {
            hotelService.release(roomId);
        }

        bookingRepository.save(booking);

        return CompletableFuture.completedFuture(null);
    }
}
