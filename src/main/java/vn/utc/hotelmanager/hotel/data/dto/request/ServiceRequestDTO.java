package vn.utc.hotelmanager.hotel.data.dto.request;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
public class ServiceRequestDTO {
    @NotNull(message = "Service id cannot be null")
    @Positive(message = "Service id must be more than 0")
    private Integer serviceId;

    @NotNull(message = "Quantity cannot be null")
    @Positive(message = "Quantity must be more than 0")
    private Integer quantity;
}
