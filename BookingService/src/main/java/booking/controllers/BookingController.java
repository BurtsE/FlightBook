package booking.controllers;

import booking.dao.Booking;
import booking.dto.CreateBookingRequestDTO;
import booking.filters.CustomUserPrincipal;
import booking.mapping.BookingMapper;
import booking.service.BookingService;
import booking.service.HotelService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bookings")
public class BookingController {

    private final BookingService bookingService;
    private final HotelService hotelService;

    public BookingController(BookingService bookingService, HotelService hotelService) {
        this.bookingService = bookingService;
        this.hotelService = hotelService;
    }

    @GetMapping("/")
    public ResponseEntity<List<Booking>> GetBookings(
            @AuthenticationPrincipal CustomUserPrincipal principal
    ){
        System.out.println(principal);

        return new ResponseEntity<>(bookingService.getBookingsByUserId(principal.getUserId()), HttpStatus.OK);
    }

    @GetMapping("/{id}") ResponseEntity<Booking> GetBooking(
            @PathVariable Long id
    ) {
        return new ResponseEntity<> (bookingService.getBookingById(id), HttpStatus.OK);
    }

    @PostMapping("/create")
    ResponseEntity<Booking> CreateBooking(
            @RequestBody CreateBookingRequestDTO requestDTO,
            @AuthenticationPrincipal CustomUserPrincipal principal
    ) {

        if (requestDTO.endDate.before(requestDTO.startDate)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Booking booking = bookingService.createBooking(requestDTO, principal.getUserId());

        if (booking == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(booking, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")ResponseEntity<Void> DeleteBooking(
            @PathVariable Long id
    ) {
        bookingService.deleteBooking(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }


}
