package vn.utc.hotelmanager.hotel.data.dto;

import lombok.Data;

import java.util.List;

@Data
public class HotelServiceRequestDTO {
    private Integer roomId;
    private List<ServiceRequestDTO> serviceRequests;
}
