package vn.utc.hotelmanager.hotel.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vn.utc.hotelmanager.hotel.data.dto.RoomFilterRequestDTO;
import vn.utc.hotelmanager.hotel.data.dto.RoomRequestDTO;
import vn.utc.hotelmanager.hotel.data.dto.RoomResponseDTO;
import vn.utc.hotelmanager.hotel.service.RoomService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/rooms")
public class RoomController {

    private final RoomService roomService;

    @Autowired
    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @GetMapping
    public ResponseEntity<List<RoomResponseDTO>> getAllRooms() {
        return new ResponseEntity<>(roomService.getAllRooms(), HttpStatus.OK);
    }

    @GetMapping("/filter")
    public ResponseEntity<List<RoomResponseDTO>> getFilteredRooms(
            @RequestBody RoomFilterRequestDTO roomRequest) {
        return new ResponseEntity<>(roomService.getFilteredRooms(roomRequest), HttpStatus.OK);
    }

    @Deprecated
    @GetMapping("/available-today")
    public ResponseEntity<List<RoomResponseDTO>> getRoomsAvailableToday() {
        LocalDateTime startTime = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        LocalDateTime endTime = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);

        return new ResponseEntity<>(
                roomService.getAvailableRoomsForDateRange(startTime, endTime), HttpStatus.OK);
    }

    @GetMapping("/available")
    public ResponseEntity<List<RoomResponseDTO>> getAvailableRoomsBetweenTime(
            @RequestParam("dateFrom") LocalDateTime dateFrom,
            @RequestParam("dateTo") LocalDateTime dateTo) {
        return new ResponseEntity<>(
                roomService.getAvailableRoomsForDateRange(dateFrom, dateTo), HttpStatus.OK);
    }

    @GetMapping("/{roomId}")
    public ResponseEntity<RoomResponseDTO> getRoom(@PathVariable("roomId") Integer roomId) {
        return new ResponseEntity<>(roomService.getOneRoom(roomId), HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> createRoom(@RequestBody RoomRequestDTO roomRequest) {
        roomService.createRoom(roomRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{roomId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> updateRoom(@PathVariable Integer roomId,
                                           @RequestBody RoomRequestDTO roomRequest) {
        roomService.updateRoom(roomId, roomRequest);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}