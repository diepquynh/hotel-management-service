package vn.utc.hotelmanager.hotel.data.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.utc.hotelmanager.hotel.model.ReceiptRoom;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Data
@NoArgsConstructor
public class BookingDetailsDTO {
    private Integer roomId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endDate;

    private Integer capacity;

    public BookingDetailsDTO(ReceiptRoom receiptRoom) {
        setRoomId(receiptRoom.getRoom().getId());
        setStartDate(LocalDateTime.ofInstant(
                receiptRoom.getArrivalTime(), ZoneId.systemDefault()
        ));
        setEndDate(LocalDateTime.ofInstant(
                receiptRoom.getLeaveTime(), ZoneId.systemDefault()
        ));
    }
}
