package vn.utc.hotelmanager.hotel.data;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.utc.hotelmanager.hotel.model.ReceiptRoomService;

public interface ReceiptRoomServiceRepository extends JpaRepository<ReceiptRoomService, Integer> {
}