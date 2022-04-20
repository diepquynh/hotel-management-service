package vn.utc.hotelmanager.hotel.data;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.utc.hotelmanager.hotel.model.ReceiptRoom;

public interface ReceiptRoomRepository extends JpaRepository<ReceiptRoom, Integer> {
}