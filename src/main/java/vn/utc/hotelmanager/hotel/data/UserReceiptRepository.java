package vn.utc.hotelmanager.hotel.data;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.utc.hotelmanager.hotel.model.UserReceipt;

import java.util.Optional;

public interface UserReceiptRepository extends JpaRepository<UserReceipt, Integer> {
    Optional<UserReceipt> findByReceiptId(Integer receiptId);
}