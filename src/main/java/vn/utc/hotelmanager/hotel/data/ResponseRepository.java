package vn.utc.hotelmanager.hotel.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.utc.hotelmanager.hotel.model.Response;

import java.util.List;

public interface ResponseRepository extends JpaRepository<Response, Integer> {

    @Query(value = "select" +
            "r.* from responses r " +
            "join users_responses ur on r.id = ur.response_id " +
            "join users u on u.id = ur.user_id " +
            "where u.username = :username", nativeQuery = true)
    List<Response> findAllByUsername(@Param("username") String username);
}