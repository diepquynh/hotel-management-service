package vn.utc.hotelmanager.hotel.data.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import vn.utc.hotelmanager.hotel.model.Booking;
import vn.utc.hotelmanager.hotel.model.Receipt;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class BookingDTO {
    private Integer bookingId;

    private String username;

    private String paymentState;

    private Boolean arrived;

    private Integer receiptId;

    private Instant created_date;

    private List<BookingDetailsDTO> bookingDetails;

    public BookingDTO(Booking booking) {
        setBookingId(booking.getId());
        setUsername(booking.getUser().getUsername());
        setPaymentState(booking.getPaymentState());
        setArrived(booking.getArrived());
        setReceiptId(booking.getReceipt().getId());
        setCreated_date(booking.getReceipt().getCreated_date());
        setBookingDetails(booking.getReceipt().getReceiptRooms()
                .stream().map(BookingDetailsDTO::new)
                .collect(Collectors.toList()));
    }
}
