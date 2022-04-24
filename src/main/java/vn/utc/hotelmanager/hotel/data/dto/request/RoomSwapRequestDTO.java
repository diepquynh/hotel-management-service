package vn.utc.hotelmanager.hotel.data.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class RoomSwapRequestDTO {
    @NotNull
    @Positive(message = "User id must be more than zero")
    private Integer userId;

    @NotNull
    @Positive(message = "Receipt id must be more than zero")
    private Integer receiptId;

    @NotNull
    @Positive(message = "User id must be more than zero")
    private Integer originalRoomId;

    @NotNull
    @Positive(message = "User id must be more than zero")
    private Integer targetRoomId;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime swapDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime extendedLeaveDate;
}
