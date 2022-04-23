package vn.utc.hotelmanager.hotel.data.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;
import vn.utc.hotelmanager.hotel.model.Response;

import java.util.Base64;

@Data
@NoArgsConstructor
public class HotelResponseDTO {
    private Integer id;
    private String content;
    private String username;

    public HotelResponseDTO(Response response) {
        setId(response.getId());
        setContent(new String(Base64.getDecoder().decode(response.getContent())));
        setUsername(response.getUser().getUsername());
    }
}
