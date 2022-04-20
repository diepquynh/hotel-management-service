package vn.utc.hotelmanager.hotel.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.utc.hotelmanager.hotel.data.dto.HotelServiceFilterRequestDTO;
import vn.utc.hotelmanager.hotel.data.dto.HotelServiceResponseDTO;
import vn.utc.hotelmanager.hotel.service.ExtrasService;

import java.util.List;

@RestController
@RequestMapping("/hotel-services")
public class HotelServiceController {

    private final ExtrasService extrasService;

    @Autowired
    public HotelServiceController(ExtrasService extrasService) {
        this.extrasService = extrasService;
    }

    @GetMapping
    public ResponseEntity<List<HotelServiceResponseDTO>> getAllServices() {
        return new ResponseEntity<>(extrasService.getAllHotelServices(), HttpStatus.OK);
    }

    @GetMapping("/filter")
    public ResponseEntity<List<HotelServiceResponseDTO>> getServicesFiltered(
            @RequestBody HotelServiceFilterRequestDTO serviceRequest) {
        return new ResponseEntity<>(extrasService.getFilteredHotelServices(serviceRequest), HttpStatus.OK);
    }

    @GetMapping("/{serviceId}")
    public ResponseEntity<HotelServiceResponseDTO> getHotelService(@PathVariable("serviceId") Integer serviceId) {
        return new ResponseEntity<>(extrasService.getHotelService(serviceId), HttpStatus.OK);
    }
}
