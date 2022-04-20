package vn.utc.hotelmanager.hotel.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import vn.utc.hotelmanager.exception.InvalidRequestException;
import vn.utc.hotelmanager.hotel.data.ServiceRepository;
import vn.utc.hotelmanager.hotel.data.dto.HotelServiceFilterRequestDTO;
import vn.utc.hotelmanager.hotel.data.dto.HotelServiceResponseDTO;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExtrasService {

    private final ServiceRepository serviceRepository;

    @Autowired
    public ExtrasService(ServiceRepository serviceRepository) {
        this.serviceRepository = serviceRepository;
    }

    public List<HotelServiceResponseDTO> getAllHotelServices() {
        return serviceRepository.findAll()
                .stream().map(HotelServiceResponseDTO::new)
                .collect(Collectors.toList());
    }

    public List<HotelServiceResponseDTO> getFilteredHotelServices(
            HotelServiceFilterRequestDTO serviceRequest) {
        verifyFilterRequest(serviceRequest);

        return serviceRepository.findFilteredServices(serviceRequest)
                .stream().map(HotelServiceResponseDTO::new)
                .collect(Collectors.toList());
    }

    public HotelServiceResponseDTO getHotelService(Integer id) {
        return serviceRepository.findById(id)
                .map(HotelServiceResponseDTO::new)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Hotel service with id %d not found", id)
                ));
    }

    private void verifyFilterRequest(HotelServiceFilterRequestDTO serviceRequest) {
        if (serviceRequest.getType() != null) {
            if (serviceRequest.getType().trim().isEmpty())
                throw new InvalidRequestException("Requested service type cannot be empty!");
        }

        if (serviceRequest.getPriceFrom() != null) {
            if (serviceRequest.getPriceFrom() < 0)
                throw new InvalidRequestException("Requested service price start cannot be less than zero");
        }

        if (serviceRequest.getPriceTo() != null) {
            if (serviceRequest.getPriceTo() < 0)
                throw new InvalidRequestException("Requested service price end cannot be less than zero");
        }

        if (serviceRequest.getPriceFrom() != null && serviceRequest.getPriceTo() != null) {
            if (serviceRequest.getPriceFrom() > serviceRequest.getPriceTo())
                throw new InvalidRequestException("Invalid requested service price range: priceFrom > priceTo");
        }
    }
}
