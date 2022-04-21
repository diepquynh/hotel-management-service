package vn.utc.hotelmanager.hotel.data.dto.response;

import lombok.Data;
import vn.utc.hotelmanager.hotel.model.Response;

@Data
public class HotelResponseDTO {
    private Integer id;
    private String content;
    private String username;

    public HotelResponseDTO(Response response) {
        setId(response.getId());
        setContent(response.getContent());
        setUsername(response.getUser().getUsername());
    }
}
