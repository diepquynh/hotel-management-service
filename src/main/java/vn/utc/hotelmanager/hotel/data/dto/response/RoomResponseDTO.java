package vn.utc.hotelmanager.hotel.data.dto.response;

import lombok.Data;
import vn.utc.hotelmanager.hotel.model.Room;
import vn.utc.hotelmanager.hotel.model.RoomType;

@Data
public class RoomResponseDTO {
    private int roomId;
    private String name;
    private String image;
    private RoomType roomType;

    public RoomResponseDTO(Room room) {
        setRoomId(room.getId());
        setName(room.getName());
        setImage(room.getImage());
        setRoomType(room.getRoomType());
    }
}
