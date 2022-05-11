package vn.utc.hotelmanager.hotel.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import vn.utc.clients.user.UserClient;
import vn.utc.clients.user.UserResponse;
import vn.utc.hotelmanager.exception.InvalidRequestException;
import vn.utc.hotelmanager.exception.RepositoryAccessException;
import vn.utc.hotelmanager.hotel.data.*;
import vn.utc.hotelmanager.hotel.data.dto.request.RoomSwapRequestDTO;
import vn.utc.hotelmanager.hotel.data.dto.response.RoomSwapItemResponseDTO;
import vn.utc.hotelmanager.hotel.model.*;
import vn.utc.hotelmanager.utils.DateUtils;

import javax.transaction.Transactional;
import java.time.Instant;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoomSwapService {

    private final RoomRepository roomRepository;
    private final RoomSwapRepository roomSwapRepository;
    private final BookingRepository bookingRepository;
    private final ReceiptRepository receiptRepository;
    private final ReceiptRoomRepository receiptRoomRepository;
    private final UserClient userClient;

    public List<RoomSwapItemResponseDTO> getRoomSwapsForBooking(Integer bookingId) {
        return roomSwapRepository.findRoomSwapsByBookingId(bookingId)
                .stream().map(RoomSwapItemResponseDTO::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public void swapRoomForUser(RoomSwapRequestDTO swapRequest) {
        if (swapRequest.getExtendedLeaveDate() != null) {
            if (swapRequest.getSwapDate().isAfter(swapRequest.getExtendedLeaveDate()))
                throw new InvalidRequestException("Swap date cannot be later than extended leave date");
        }

        UserResponse thisUser = Optional.of(userClient.getUserById(swapRequest.getUserId()))
                .orElseThrow(
                        () -> new ResourceNotFoundException(
                                String.format("User with id %d does not exist",
                                        swapRequest.getUserId())
                        )
                );

        Booking thisUserBooking = bookingRepository.findByReceiptId(swapRequest.getReceiptId())
                .orElseThrow(
                        () -> new ResourceNotFoundException(
                                String.format("Booking with receipt id %d does not exist",
                                        swapRequest.getUserId())
                        )
                );

        if (thisUserBooking.getUser().getId() != thisUser.getId())
            throw new InvalidRequestException(
                    String.format("Receipt with id %d does not belong to user with id %d",
                            swapRequest.getReceiptId(), swapRequest.getUserId())
            );

        Room originalRoom = roomRepository.findById(swapRequest.getOriginalRoomId())
                .orElseThrow(
                        () -> new ResourceNotFoundException(
                                String.format("Room with id %d does not exist",
                                        swapRequest.getUserId())
                        )
                );

        Receipt thisBookingReceipt = thisUserBooking.getReceipt();
        ReceiptRoom originalRoomReceipt = thisBookingReceipt.getReceiptRooms()
                .stream().filter(receiptRoom -> receiptRoom.getRoom().getId().equals(swapRequest.getOriginalRoomId()))
                .findFirst().orElse(null);

        if (originalRoomReceipt == null)
            throw new InvalidRequestException(
                    String.format("Room with id %d has not been booked by user with id %d",
                            originalRoom.getId(), thisUser.getId())
            );

        Room targetRoomForSwapping = roomRepository.findById(swapRequest.getTargetRoomId())
                .orElseThrow(
                        () -> new ResourceNotFoundException(
                                String.format("Room with id %d does not exist",
                                        swapRequest.getTargetRoomId())
                        )
                );

        List<ReceiptRoom> targetRoomReceipts =
                receiptRoomRepository.findByRoom(targetRoomForSwapping);

        if (!CollectionUtils.isEmpty(targetRoomReceipts)) {
            boolean swapConflict =
                    targetRoomReceipts.stream().anyMatch(
                            receiptRoom ->
                                    receiptRoom.isReceiptRoomConflictBetweenDates(
                                            swapRequest.getSwapDate(),
                                            swapRequest.getExtendedLeaveDate() != null ?
                                                    swapRequest.getExtendedLeaveDate() :
                                                    originalRoomReceipt.getLeaveTime()
                                                            .atZone(ZoneId.systemDefault()).toLocalDateTime()
                                    )
                    );

            if (swapConflict)
                throw new InvalidRequestException(
                        "Someone has booked this room between swap date and extended leave date"
                );
        }

        Instant instantSwapDate = swapRequest.getSwapDate().atZone(ZoneId.systemDefault()).toInstant();
        Instant instantLeaveDate = swapRequest.getExtendedLeaveDate() != null ?
                swapRequest.getExtendedLeaveDate().atZone(ZoneId.systemDefault()).toInstant() :
                originalRoomReceipt.getLeaveTime();

        double originalRoomTotalBalance =
                DateUtils.daysBetween(originalRoomReceipt.getArrivalTime(), originalRoomReceipt.getLeaveTime()) *
                originalRoom.getRoomType().getPrice();
        double newOriginalRoomTotalBalance =
                DateUtils.daysBetween(originalRoomReceipt.getArrivalTime(), instantSwapDate) *
                        originalRoom.getRoomType().getPrice();
        double newSwappedRoomTotalBalance = DateUtils.daysBetween(instantSwapDate, instantLeaveDate) *
                targetRoomForSwapping.getRoomType().getPrice();

        ReceiptRoom newRoomReceipt =
                ReceiptRoom.builder()
                        .receipt(thisBookingReceipt)
                        .room(targetRoomForSwapping)
                        .arrivalTime(instantSwapDate)
                        .leaveTime(instantLeaveDate)
                        .capacity(originalRoomReceipt.getCapacity())
                        .build();

        thisBookingReceipt.addTotalBalance(
                -originalRoomTotalBalance + newOriginalRoomTotalBalance +
                        newSwappedRoomTotalBalance
        );

        RoomSwap newRoomSwap =
                RoomSwap.builder()
                        .receipt(thisBookingReceipt)
                        .originalRoom(originalRoom)
                        .targetRoom(targetRoomForSwapping)
                        .swapDate(instantSwapDate)
                        .createdDate(Instant.now())
                        .build();

        try {
            roomSwapRepository.save(newRoomSwap);
            receiptRepository.save(thisBookingReceipt);
            receiptRoomRepository.save(originalRoomReceipt);
            receiptRoomRepository.save(newRoomReceipt);
        } catch (Exception e) {
            throw new RepositoryAccessException(
                    String.format("Unable to swap rooms for user with id %d: %s",
                            swapRequest.getUserId(), e.getMessage())
            );
        }
    }
}
