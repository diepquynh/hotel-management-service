package vn.utc.hotelmanager.hotel.data.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;
import vn.utc.hotelmanager.hotel.model.Review;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;

@Data
@NoArgsConstructor
public class HotelReviewDTO {
    private Integer id;
    private LocalDateTime created_date;
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
