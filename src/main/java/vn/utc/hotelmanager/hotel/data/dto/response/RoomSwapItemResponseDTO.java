package vn.utc.hotelmanager.hotel.data.dto.response;

import lombok.Data;
import vn.utc.hotelmanager.hotel.model.RoomSwap;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Data
public class RoomSwapItemResponseDTO {
    private Integer roomSwapId;
    private Integer originalRoomId;
    private Integer targetRoomId;
    private LocalDateTime swapDate;
    private LocalDateTime createdDate;

    public RoomSwapItemResponseDTO(RoomSwap roomSwap) {
        setRoomSwapId(roomSwap.getId());
        setOriginalRoomId(roomSwap.getOriginalRoom().getId());
        setTargetRoomId(roomSwap.getTargetRoom().getId());
        setSwapDate(roomSwap.getSwapDate()
                .atZone(ZoneId.systemDefault()).toLocalDateTime());
        setCreatedDate(roomSwap.getCreatedDate()
                .atZone(ZoneId.systemDefault()).toLocalDateTime());
    }
}
