package vn.utc.hotelmanager.hotel.data;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.utc.hotelmanager.hotel.model.Receipt;

public interface ReceiptRepository extends JpaRepository<Receipt, Integer> {
}