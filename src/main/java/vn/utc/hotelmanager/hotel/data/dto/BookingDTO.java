package vn.utc.hotelmanager.hotel.data.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import vn.utc.hotelmanager.hotel.model.Receipt;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class BookingDTO {
    private Integer receiptId;

    private Instant created_date;

    private List<BookingDetailsDTO> bookingDetails;

    public BookingDTO(Receipt receipt) {
        setReceiptId(receipt.getId());
        setCreated_date(receipt.getCreated_date());
        setBookingDetails(receipt.getReceiptRooms().stream().map(BookingDetailsDTO::new)
                .collect(Collectors.toList()));
    }
}
