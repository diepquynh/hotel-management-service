package vn.utc.hotelmanager.hotel.data.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.utc.hotelmanager.hotel.model.ReceiptRoom;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Data
@NoArgsConstructor
public class BookingDetailsDTO {

    @NotNull(message = "Room id cannot be null")
    @Positive(message = "Id must be more than 0")
    private Integer roomId;

    @NotNull(message = "Start date cannot be null")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startDate;

    @NotNull(message = "End date cannot be null")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endDate;

    @NotNull(message = "Capacity cannot be null")
    @Positive(message = "Capacity must be more than 0")
    private Integer capacity;

    public BookingDetailsDTO(ReceiptRoom receiptRoom) {
        setRoomId(receiptRoom.getRoom().getId());
        setStartDate(LocalDateTime.ofInstant(
                receiptRoom.getArrivalTime(), ZoneId.systemDefault()
        ));
        setEndDate(LocalDateTime.ofInstant(
                receiptRoom.getLeaveTime(), ZoneId.systemDefault()
        ));
        setCapacity(receiptRoom.getCapacity());
    }
}
