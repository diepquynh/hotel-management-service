package vn.utc.hotelmanager.hotel.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.utc.hotelmanager.hotel.model.Booking;
import vn.utc.hotelmanager.hotel.model.Receipt;

import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Integer> {
    Optional<Booking> findByReceiptId(Integer receiptId);

    @Query(value = "select b.* from bookings b " +
            "inner join users u on b.user_id = u.id " +
            "where u.username = :username", nativeQuery = true)
    List<Booking> findBookingsByUsername(@Param("username") String username);
}