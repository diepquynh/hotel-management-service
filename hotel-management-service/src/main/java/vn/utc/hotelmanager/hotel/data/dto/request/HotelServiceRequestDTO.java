package vn.utc.hotelmanager.hotel.data.dto.request;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.List;

@Data
public class HotelServiceRequestDTO {
    @NotNull(message = "Room id cannot be null")
    @Positive(message = "Room id must be more than 0")
    private Integer roomId;

    private List<@Valid ServiceRequestDTO> serviceRequests;
}
