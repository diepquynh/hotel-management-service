package vn.utc.hotelmanager.hotel.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.utc.hotelmanager.hotel.model.Receipt;

import java.util.List;

public interface ReceiptRepository extends JpaRepository<Receipt, Integer> {

    @Query(value = "select r.*, ur.user_id from receipts r " +
            "inner join users_receipts ur on r.id = ur.receipt_id " +
            "inner join users u on ur.user_id = u.id " +
            "where u.username = :username", nativeQuery = true)
    List<Receipt> findReceiptsByUsername(@Param("username") String username);
}