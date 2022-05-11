package vn.utc.hotelmanager.hotel.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.utc.hotelmanager.hotel.data.dto.HotelServiceItemDTO;
import vn.utc.hotelmanager.hotel.data.dto.request.HotelServiceFilterRequestDTO;
import vn.utc.hotelmanager.hotel.data.dto.request.HotelServiceRequestDTO;
import vn.utc.hotelmanager.hotel.service.ExtrasService;

import javax.validation.Valid;
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
    public ResponseEntity<List<HotelServiceItemDTO>> getAllServices() {
        return new ResponseEntity<>(extrasService.getAllHotelServices(), HttpStatus.OK);
    }

    @PostMapping("/filter")
    public ResponseEntity<List<HotelServiceItemDTO>> getServicesFiltered(
            @Valid @RequestBody HotelServiceFilterRequestDTO serviceRequest) {
        return new ResponseEntity<>(extrasService.getFilteredHotelServices(serviceRequest), HttpStatus.OK);
    }

    @GetMapping("/{serviceId}")
    public ResponseEntity<HotelServiceItemDTO> getHotelService(@PathVariable("serviceId") Integer serviceId) {
        return new ResponseEntity<>(extrasService.getHotelService(serviceId), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Void> addService(
            @Valid @RequestBody HotelServiceItemDTO serviceItem) {
        extrasService.addService(serviceItem);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping
    public ResponseEntity<Void> updateService(
            @Valid @RequestBody HotelServiceItemDTO serviceItem) {
        extrasService.updateService(serviceItem);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/{serviceId}")
    public ResponseEntity<Void> deleteService(@PathVariable("serviceId") Integer serviceId) {
        extrasService.deleteService(serviceId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("/request-for-room")
    public ResponseEntity<Void> requestServiceForRoom(
            @Valid @RequestBody HotelServiceRequestDTO serviceRequest) {
        extrasService.requestServiceForRoom(serviceRequest);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
