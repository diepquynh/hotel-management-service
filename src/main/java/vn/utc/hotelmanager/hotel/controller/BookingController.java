package vn.utc.hotelmanager.hotel.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vn.utc.hotelmanager.hotel.data.dto.BookingDTO;
import vn.utc.hotelmanager.hotel.data.dto.request.BookingUpdateRequestDTO;
import vn.utc.hotelmanager.hotel.service.BookingService;

import java.util.List;

@RestController
@RequestMapping("/bookings")
public class BookingController {

    private final BookingService bookingService;

    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<BookingDTO>> getAllBookings() {
        return new ResponseEntity<>(bookingService.getAllBookings(), HttpStatus.OK);
    }

    @GetMapping("/user-bookings")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<List<BookingDTO>> getUserBookings() {
        return new ResponseEntity<>(bookingService.getUserBookings(), HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<Void> bookThisRoom(@RequestBody BookingDTO bookingRequest) {
        bookingService.BookThisRoom(bookingRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/arrived")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<Void> guestHasArrived(@RequestBody BookingUpdateRequestDTO updateRequest) {
        bookingService.guestHasArrived(updateRequest);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<Void> deleteThisBooking(@RequestBody BookingUpdateRequestDTO updateRequest) {

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
