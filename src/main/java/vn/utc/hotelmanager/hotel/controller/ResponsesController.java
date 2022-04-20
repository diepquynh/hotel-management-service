package vn.utc.hotelmanager.hotel.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vn.utc.hotelmanager.hotel.data.dto.HotelResponseDTO;
import vn.utc.hotelmanager.hotel.service.ResponseService;

import java.util.List;

@RestController
@RequestMapping("/responses")
public class ResponsesController {

    @Autowired
    private ResponseService responseService;

    @GetMapping
    public ResponseEntity<List<HotelResponseDTO>> getAllResponses() {
        return new ResponseEntity<>(responseService.getAllUserResponses(), HttpStatus.OK);
    }

    @GetMapping("/user")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<List<HotelResponseDTO>> getUserResponses() {
        return new ResponseEntity<>(responseService.getUserResponses(), HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<Void> createUserResponse(@RequestBody HotelResponseDTO userResponse) {
        responseService.createUserResponse(userResponse.getContent());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
