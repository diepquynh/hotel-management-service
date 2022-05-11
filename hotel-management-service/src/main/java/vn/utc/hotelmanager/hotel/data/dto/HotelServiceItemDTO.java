package vn.utc.hotelmanager.hotel.data.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import vn.utc.hotelmanager.hotel.model.Service;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;

@Data
@NoArgsConstructor
public class HotelServiceItemDTO {
    private Integer id;

    @NotBlank
    private String name;

    @PositiveOrZero(message = "Price must be more or equal to 0")
    private Double price;

    @NotBlank
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
