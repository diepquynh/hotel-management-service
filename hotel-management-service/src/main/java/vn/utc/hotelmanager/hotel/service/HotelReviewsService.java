package vn.utc.hotelmanager.hotel.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import vn.utc.clients.user.UserClient;
import vn.utc.clients.user.UserRequest;
import vn.utc.clients.user.UserResponse;
import vn.utc.hotelmanager.hotel.model.User;
import vn.utc.hotelmanager.exception.InvalidRequestException;
import vn.utc.hotelmanager.exception.RepositoryAccessException;
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
    private final UserClient userClient;

    @Autowired
    public HotelReviewsService(ReviewRepository reviewRepository, UserClient userClient) {
        this.reviewRepository = reviewRepository;
        this.userClient = userClient;
    }

    public List<HotelReviewDTO> getAllUserReviews() {
        return reviewRepository.findAll()
                .stream().map(HotelReviewDTO::new)
                .collect(Collectors.toList());
    }

    public List<HotelReviewDTO> getUserReviews(String username) {
        return reviewRepository.findAllByUsername(username)
                .stream().map(HotelReviewDTO::new)
                .collect(Collectors.toList());
    }

    public void createUserReview(String username, String content) {
        if (content == null)
            throw new InvalidRequestException("Review content cannot be empty!");

        UserResponse currentUser = userClient.getUserByUsername(new UserRequest(username));

        Review newReview = Review.builder()
                .created_date(Instant.now())
                .content(Base64.getEncoder().encodeToString(content.getBytes()))
                .user(User.builder().id(currentUser.getId()).build())
                .build();

        try {
            reviewRepository.save(newReview);
        } catch (Exception e) {
            throw new RepositoryAccessException("Cannot save this review: " + e.getMessage());
        }
    }

    public void deleteUserReview(Integer reviewId) {
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
