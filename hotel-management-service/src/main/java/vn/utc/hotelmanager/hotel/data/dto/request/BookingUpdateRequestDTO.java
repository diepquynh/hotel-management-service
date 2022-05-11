package vn.utc.hotelmanager.hotel.data.dto.request;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
public class BookingUpdateRequestDTO {

    @NotNull(message = "User id cannot be null")
    @Positive(message = "User id must be more than 0")
    private Integer userId;

    @NotNull(message = "Booking id cannot be null")
    @Positive(message = "Booking id must be more than 0")
    private Integer bookingId;
}
