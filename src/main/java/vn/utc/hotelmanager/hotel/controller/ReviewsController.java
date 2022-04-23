package vn.utc.hotelmanager.hotel.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vn.utc.hotelmanager.hotel.data.dto.response.HotelReviewDTO;
import vn.utc.hotelmanager.hotel.service.HotelReviewsService;

import java.util.List;

@RestController
@RequestMapping("/reviews")
public class ReviewsController {

    @Autowired
    private HotelReviewsService hotelReviewsService;

    @GetMapping
    public ResponseEntity<List<HotelReviewDTO>> getAllReviews() {
        return new ResponseEntity<>(hotelReviewsService.getAllUserReviews(), HttpStatus.OK);
    }

    @GetMapping("/user")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<List<HotelReviewDTO>> getUserReviews() {
        return new ResponseEntity<>(hotelReviewsService.getUserReviews(), HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<Void> createUserReview(@RequestBody HotelReviewDTO userReview) {
        hotelReviewsService.createUserReview(userReview.getContent());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
