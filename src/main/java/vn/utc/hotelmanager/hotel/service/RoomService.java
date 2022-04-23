package vn.utc.hotelmanager.hotel.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import vn.utc.hotelmanager.exception.InvalidRequestException;
import vn.utc.hotelmanager.exception.RepositoryAccessException;
import vn.utc.hotelmanager.exception.ResourceAlreadyExistedException;
import vn.utc.hotelmanager.hotel.data.RoomRepository;
import vn.utc.hotelmanager.hotel.data.RoomTypeRepository;
import vn.utc.hotelmanager.hotel.data.dto.request.RoomFilterRequestDTO;
import vn.utc.hotelmanager.hotel.data.dto.request.RoomRequestDTO;
import vn.utc.hotelmanager.hotel.data.dto.response.RoomResponseDTO;
import vn.utc.hotelmanager.hotel.model.ReceiptRoom;
import vn.utc.hotelmanager.hotel.model.Room;
import vn.utc.hotelmanager.hotel.model.RoomType;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RoomService {

    private final RoomRepository roomRepository;
    private final RoomTypeRepository roomTypeRepository;

    @Autowired
    public RoomService(RoomRepository roomRepository, RoomTypeRepository roomTypeRepository) {
        this.roomRepository = roomRepository;
        this.roomTypeRepository = roomTypeRepository;
    }

    public List<RoomResponseDTO> getAllRooms() {
        return roomRepository.findAll().stream()
                .map(RoomResponseDTO::new)
                .collect(Collectors.toList());
    }

    public RoomResponseDTO getOneRoom(Integer id) {
        return roomRepository.findById(id)
                .map(RoomResponseDTO::new)
                .orElseThrow(
                () -> new InvalidRequestException(
                        String.format("Room with id %d does not exist", id)
                )
        );
    }

    public List<RoomResponseDTO> getFilteredRooms(RoomFilterRequestDTO roomRequest) {
        verifyFilterRequest(roomRequest);

        return roomRepository.findFilteredRooms(roomRequest).stream()
                .map(RoomResponseDTO::new)
                .collect(Collectors.toList());
    }

    public List<RoomResponseDTO> getAvailableRoomsForDateRange(
            LocalDateTime startDate, LocalDateTime endDate
    ) {
        List<RoomResponseDTO> roomResponseDTOS = new ArrayList<>();
        List<Room> availableRooms = roomRepository.
                findAvailableRoomBetweenTime(startDate, endDate);

        if (!CollectionUtils.isEmpty(availableRooms))
            roomResponseDTOS = availableRooms.stream()
                    .map(RoomResponseDTO::new)
                    .collect(Collectors.toList());

        return roomResponseDTOS;
    }

    @Transactional
    public void createRoom(RoomRequestDTO roomRequest) {
        verifyCreateRequest(roomRequest);
        RoomType roomType = roomTypeRepository.findById(roomRequest.getRoomTypeId()).orElseThrow(
                () -> new InvalidRequestException(
                        String.format("Room type id %d does not exist", roomRequest.getRoomTypeId())
                )
        );

        Room newRoom =
                Room.builder()
                        .name(roomRequest.getName())
                        .image(roomRequest.getImage())
                        .roomType(roomType)
                        .build();

        try {
            roomRepository.save(newRoom);
        } catch (Exception e) {
            throw new RepositoryAccessException("Cannot save this room: " + e.getMessage());
        }
    }

    public void updateRoom(Integer roomId, RoomRequestDTO roomRequest) {
        verifyCreateRequest(roomRequest);
        Room room = roomRepository.findById(roomId).orElseThrow(
                () -> new ResourceNotFoundException(
                        String.format("Room with id %d does not exist", roomId)
                )
        );
        RoomType roomType = roomTypeRepository.findById(roomRequest.getRoomTypeId()).orElseThrow(
                () -> new InvalidRequestException(
                        String.format("Room type id %d does not exist", roomRequest.getRoomTypeId())
                )
        );

        room.setName(roomRequest.getName());
        room.setImage(roomRequest.getImage());
        room.setRoomType(roomType);

        try {
            roomRepository.save(room);
        } catch (Exception e) {
            throw new RepositoryAccessException("Cannot update this room: " + e.getMessage());
        }
    }

    public void deleteRoom(Integer roomId) {
        Room room = roomRepository.findById(roomId).orElseThrow(
                () -> new ResourceNotFoundException(
                        String.format("Room with id %d does not exist", roomId)
                )
        );

        // Check if the target room has someone living
        ReceiptRoom receiptRoom = room.getActiveReceiptRoom();
        if (receiptRoom != null && receiptRoom.getReceipt().getBooking().getArrived().equals(true))
            throw new InvalidRequestException("Someone is currently living this room. Please try again later");

        try {
            roomRepository.delete(room);
        } catch (Exception e) {
            throw new RepositoryAccessException("Cannot delete this room: " + e.getMessage());
        }
    }

    private void verifyCreateRequest(RoomRequestDTO roomRequest) {
        Optional.ofNullable(
                roomRepository.findByName(roomRequest.getName())
        ).orElseThrow(
                () -> new ResourceAlreadyExistedException(
                        String.format("Room with name %s already existed", roomRequest.getName())
                )
        );
    }

    private void verifyFilterRequest(RoomFilterRequestDTO roomRequest) {
        if (roomRequest.getType() != null) {
            roomTypeRepository
                    .findByName(roomRequest.getType())
                    .orElseThrow(() ->
                            new InvalidRequestException("Invalid room type")
                    );
        }

        if (roomRequest.getPriceFrom() != null && roomRequest.getPriceTo() != null) {
            if (roomRequest.getPriceFrom() > roomRequest.getPriceTo())
                throw new InvalidRequestException("Invalid price range: priceFrom > priceTo");
        }

        if (roomRequest.getSeatCountFrom() != null && roomRequest.getSeatCountTo() != null) {
            if (roomRequest.getSeatCountFrom() > roomRequest.getSeatCountTo())
                throw new InvalidRequestException("Invalid seat count range: seatCountFrom > seatCountTo");
        }
    }

}
