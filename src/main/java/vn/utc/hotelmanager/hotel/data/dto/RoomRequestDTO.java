package vn.utc.hotelmanager.hotel.data.dto;

import lombok.Data;

@Data
public class RoomRequestDTO {
    private String name;
    private String image;
    private Integer roomTypeId;
}
