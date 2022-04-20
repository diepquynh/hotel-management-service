package vn.utc.hotelmanager.hotel.data.dto;

import lombok.Data;
import vn.utc.hotelmanager.hotel.model.Service;

@Data
public class HotelServiceResponseDTO {
    private Integer id;
    private String name;
    private Double price;
    private String type;
    private String image;

    public HotelServiceResponseDTO(Service service) {
        setId(service.getId());
        setName(service.getName());
        setPrice(service.getPrice());
        setType(service.getType());
        setImage(service.getImage());
    }
}
