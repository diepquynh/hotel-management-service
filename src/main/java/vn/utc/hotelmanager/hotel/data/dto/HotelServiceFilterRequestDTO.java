package vn.utc.hotelmanager.hotel.data.dto;

import lombok.Data;

@Data
public class HotelServiceFilterRequestDTO {
    private String type;
    private Double priceFrom;
    private Double priceTo;
}
