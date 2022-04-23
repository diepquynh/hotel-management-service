package vn.utc.hotelmanager.hotel.data.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import vn.utc.hotelmanager.hotel.model.Service;

@Data
@NoArgsConstructor
public class HotelServiceItemDTO {
    private Integer id;
    private String name;
    private Double price;
    private String type;
    private String image;

    public HotelServiceItemDTO(Service service) {
        setId(service.getId());
        setName(service.getName());
        setPrice(service.getPrice());
        setType(service.getType());
        setImage(service.getImage());
    }
}
