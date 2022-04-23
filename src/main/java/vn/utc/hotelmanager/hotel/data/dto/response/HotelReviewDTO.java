package vn.utc.hotelmanager.hotel.data.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;
import vn.utc.hotelmanager.hotel.model.Review;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;

@Data
@NoArgsConstructor
public class HotelReviewDTO {

    @Positive(message = "Review id must be more than 0")
    private Integer id;

    private LocalDateTime created_date;

    @NotEmpty
    private String content;

    private String username;

    public HotelReviewDTO(Review review) {
        setId(review.getId());
        setCreated_date(review.getCreated_date().
                atZone(ZoneId.systemDefault()).toLocalDateTime());
        setContent(new String(Base64.getDecoder().decode(review.getContent())));
        setUsername(review.getUser().getUsername());
    }
}
