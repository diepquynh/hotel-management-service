package vn.utc.hotelmanager.hotel.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.utc.hotelmanager.hotel.data.dto.request.RoomSwapRequestDTO;
import vn.utc.hotelmanager.hotel.data.dto.response.RoomSwapItemResponseDTO;
import vn.utc.hotelmanager.hotel.service.RoomSwapService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/room-swaps")
public class RoomSwapController {

    private final RoomSwapService roomSwapService;

    @Autowired
    public RoomSwapController(RoomSwapService roomSwapService) {
        this.roomSwapService = roomSwapService;
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<List<RoomSwapItemResponseDTO>> getRoomSwapsForBooking(
            @PathVariable("bookingId") Integer bookingId) {
        return new ResponseEntity<>(roomSwapService.getRoomSwapsForBooking(bookingId), HttpStatus.OK);
    }

    @PostMapping("/begin-swap")
    public ResponseEntity<Void> swapRoomForUser(
            @Valid @RequestBody RoomSwapRequestDTO swapRequest) {
        roomSwapService.swapRoomForUser(swapRequest);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
