package vn.utc.hotelmanager.hotel.data.dto;

import lombok.Data;

@Data
public class RoomFilterRequestDTO {
    private String type;
    private Double priceFrom;
    private Double priceTo;
    private Integer seatCountFrom;
    private Integer seatCountTo;
}
