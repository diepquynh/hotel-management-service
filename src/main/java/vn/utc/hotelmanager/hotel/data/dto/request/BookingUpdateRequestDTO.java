package vn.utc.hotelmanager.hotel.data.dto.request;

import lombok.Data;

@Data
public class BookingUpdateRequestDTO {
    private Integer userId;
    private Integer receiptId;
    private Boolean arrived;
}
