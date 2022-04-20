package vn.utc.hotelmanager.hotel.data;

import vn.utc.hotelmanager.hotel.data.dto.RoomFilterRequestDTO;
import vn.utc.hotelmanager.hotel.model.Room;

import java.util.List;

public interface CustomRoomRepository {
    List<Room> findFilteredRooms(RoomFilterRequestDTO roomRequest);
}
