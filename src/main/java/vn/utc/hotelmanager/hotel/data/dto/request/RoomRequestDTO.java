package vn.utc.hotelmanager.hotel.data.dto.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
public class RoomRequestDTO {
    @NotNull
    @NotBlank
    private String name;

    private String image;

    @NotNull(message = "Room type id cannot be null")
    @Positive(message = "Room type id must be more than 0")
    private Integer roomTypeId;
}
