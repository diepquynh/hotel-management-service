package vn.utc.hotelmanager.hotel.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.utc.hotelmanager.hotel.data.dto.BookingDTO;
import vn.utc.hotelmanager.hotel.data.dto.request.BookingUpdateRequestDTO;
import vn.utc.hotelmanager.hotel.service.BookingService;

import javax.validation.Valid;
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
    public ResponseEntity<List<BookingDTO>> getAllBookings() {
        return new ResponseEntity<>(bookingService.getAllBookings(), HttpStatus.OK);
    }

    @GetMapping("/user-bookings")
    public ResponseEntity<List<BookingDTO>> getUserBookings(@RequestHeader("X-Username") String username) {
        return new ResponseEntity<>(bookingService.getUserBookings(username), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Void> bookThisRoom(@RequestHeader("X-Username") String username,
                                             @Valid @RequestBody BookingDTO bookingRequest) {
        bookingService.BookThisRoom(username, bookingRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/admin/arrived")
    public ResponseEntity<Void> guestHasArrived(
            @Valid @RequestBody BookingUpdateRequestDTO updateRequest) {
        bookingService.guestHasArrived(updateRequest);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteThisBooking(@RequestHeader("X-Username") String username,
                                                  @Valid @RequestBody BookingUpdateRequestDTO deletionRequest) {
        bookingService.deleteThisBooking(username, deletionRequest, false);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/admin")
    public ResponseEntity<Void> deleteThisBookingAdmin(
            @Valid @RequestBody BookingUpdateRequestDTO deletionRequest) {
        bookingService.deleteThisBookingAdmin(deletionRequest);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
