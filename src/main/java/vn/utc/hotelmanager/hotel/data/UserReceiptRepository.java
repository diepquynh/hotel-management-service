package vn.utc.hotelmanager.hotel.data;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.utc.hotelmanager.hotel.model.Booking;

import java.util.Optional;

public interface UserReceiptRepository extends JpaRepository<Booking, Integer> {
    Optional<Booking> findByReceiptId(Integer receiptId);
}