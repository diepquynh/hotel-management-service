package vn.utc.hotelmanager.hotel.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.utc.hotelmanager.hotel.data.dto.response.HotelReviewDTO;
import vn.utc.hotelmanager.hotel.service.HotelReviewsService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
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
    public ResponseEntity<List<HotelReviewDTO>> getUserReviews(@RequestHeader("X-Username") String username) {
        return new ResponseEntity<>(hotelReviewsService.getUserReviews(username), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Void> createUserReview(
            @RequestHeader("X-Username") String username,
            @Valid @RequestBody HotelReviewDTO userReview) {
        hotelReviewsService.createUserReview(username, userReview.getContent());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Void> deleteUserReview(
            @Positive(message = "Review id must be more than 0")
            @PathVariable("reviewId") Integer reviewId) {
        hotelReviewsService.deleteUserReview(reviewId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
