package vn.utc.hotelmanager.hotel.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import vn.utc.hotelmanager.exception.InvalidRequestException;
import vn.utc.hotelmanager.exception.RepositoryAccessException;
import vn.utc.hotelmanager.auth.user.data.UserRepository;
import vn.utc.hotelmanager.auth.user.model.User;
import vn.utc.hotelmanager.utils.ApplicationUserService;
import vn.utc.hotelmanager.hotel.data.ReviewRepository;
import vn.utc.hotelmanager.hotel.data.dto.response.HotelReviewDTO;
import vn.utc.hotelmanager.hotel.model.Review;

import java.time.Instant;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class HotelReviewsService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;

    @Autowired
    public HotelReviewsService(ReviewRepository reviewRepository, UserRepository userRepository) {
        this.reviewRepository = reviewRepository;
        this.userRepository = userRepository;
    }

    public List<HotelReviewDTO> getAllUserReviews() {
        return reviewRepository.findAll()
                .stream().map(HotelReviewDTO::new)
                .collect(Collectors.toList());
    }

    public List<HotelReviewDTO> getUserReviews() {
        String currentUsername = ApplicationUserService.getCurrentUser().getUsername();
        return reviewRepository.findAllByUsername(currentUsername)
                .stream().map(HotelReviewDTO::new)
                .collect(Collectors.toList());
    }

    public void createUserReview(String content) {
        if (content == null || content.trim().isEmpty())
            throw new InvalidRequestException("Review content cannot be empty!");

        String currentUsername = ApplicationUserService.getCurrentUser().getUsername();
        User currentUser = userRepository.findByUsername(currentUsername);

        Review newReview = Review.builder()
                .created_date(Instant.now())
                .content(Base64.getEncoder().encodeToString(content.getBytes()))
                .user(currentUser)
                .build();

        try {
            reviewRepository.save(newReview);
        } catch (Exception e) {
            throw new RepositoryAccessException("Cannot save this review: " + e.getMessage());
        }
    }

    public void deleteUserReview(Integer reviewId) {
        if (reviewId < 0)
            throw new InvalidRequestException("Invalid review id: value cannot be less than zero");

        Review userReview = reviewRepository.findById(reviewId).orElseThrow(
                () -> new ResourceNotFoundException(
                        String.format("Review with id %d does not exist", reviewId)
                )
        );

        try {
            reviewRepository.delete(userReview);
        } catch (Exception e) {
            throw new RepositoryAccessException(
                    String.format("Unable to delete review with id %d: %s",
                            reviewId, e.getMessage())
            );
        }
    }
}
