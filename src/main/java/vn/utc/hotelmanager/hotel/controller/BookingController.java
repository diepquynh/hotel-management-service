package vn.utc.hotelmanager.hotel.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vn.utc.hotelmanager.hotel.data.dto.BookingRequestDTO;
import vn.utc.hotelmanager.hotel.service.BookingService;

@RestController
@RequestMapping("/bookings")
public class BookingController {

    private final BookingService bookingService;

    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<Void> bookThisRoom(@RequestBody BookingRequestDTO bookingRequest) {
        bookingService.BookThisRoom(bookingRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
