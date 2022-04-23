package vn.utc.hotelmanager.hotel.data.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;
import vn.utc.hotelmanager.hotel.model.Review;

import java.util.Base64;

@Data
@NoArgsConstructor
public class HotelReviewDTO {
    private Integer id;
    private String content;
    private String username;

    public HotelReviewDTO(Review review) {
        setId(review.getId());
        setContent(new String(Base64.getDecoder().decode(review.getContent())));
        setUsername(review.getUser().getUsername());
    }
}
