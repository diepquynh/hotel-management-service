package vn.utc.hotelmanager.hotel.data;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.utc.hotelmanager.hotel.model.ReceiptRoom;
import vn.utc.hotelmanager.hotel.model.Room;

import java.util.List;

public interface ReceiptRoomRepository extends JpaRepository<ReceiptRoom, Integer> {
    List<ReceiptRoom> findByRoom(Room room);
}