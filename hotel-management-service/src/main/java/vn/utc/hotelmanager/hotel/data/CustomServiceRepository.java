package vn.utc.hotelmanager.hotel.data;

import vn.utc.hotelmanager.hotel.data.dto.request.HotelServiceFilterRequestDTO;
import vn.utc.hotelmanager.hotel.model.Service;

import java.util.List;

public interface CustomServiceRepository {
    List<Service> findFilteredServices(HotelServiceFilterRequestDTO serviceRequest);
}
