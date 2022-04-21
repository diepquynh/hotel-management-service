package vn.utc.hotelmanager.hotel.data.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class BookingDTO {
    private List<BookingDetailsDTO> bookingDetails;
}
