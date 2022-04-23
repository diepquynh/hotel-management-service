package vn.utc.hotelmanager.hotel.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.utc.hotelmanager.hotel.model.Receipt;

import java.util.List;

public interface ReceiptRepository extends JpaRepository<Receipt, Integer> {
}