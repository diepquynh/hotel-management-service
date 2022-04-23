package vn.utc.hotelmanager.hotel.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.utc.hotelmanager.hotel.model.Review;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Integer> {

    @Query(value = "select" +
            "r.* from reviews r " +
            "join users_reviews ur on r.id = ur.review_id " +
            "join users u on u.id = ur.user_id " +
            "where u.username = :username", nativeQuery = true)
    List<Review> findAllByUsername(@Param("username") String username);
}