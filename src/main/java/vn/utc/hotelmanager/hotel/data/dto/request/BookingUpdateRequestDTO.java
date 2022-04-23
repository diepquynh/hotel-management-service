package vn.utc.hotelmanager.hotel.data.dto.request;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
public class BookingUpdateRequestDTO {

    @NotNull(message = "User id cannot be null")
    @Positive(message = "User id must be more than 0")
    private Integer userId;

    @NotNull(message = "Receipt id cannot be null")
    @Positive(message = "Receipt id must be more than 0")
    private Integer receiptId;

    @NotNull(message = "Arrival state cannot be null")
    private Boolean arrived;
}
