package vn.utc.hotelmanager.hotel.data.dto.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;

@Data
public class HotelServiceFilterRequestDTO {
    @NotBlank
    private String type;

    @PositiveOrZero(message = "Starting price must be more or equal to 0")
    private Double priceFrom;

    @PositiveOrZero(message = "Ending price must be more or equal to 0")
    private Double priceTo;
}
