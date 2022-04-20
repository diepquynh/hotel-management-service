package vn.utc.hotelmanager.hotel.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import vn.utc.hotelmanager.auth.user.data.UserRepository;
import vn.utc.hotelmanager.auth.user.model.User;
import vn.utc.hotelmanager.auth.user.service.ApplicationUserService;
import vn.utc.hotelmanager.exception.InvalidRequestException;
import vn.utc.hotelmanager.exception.RepositoryAccessException;
import vn.utc.hotelmanager.hotel.data.ReceiptRepository;
import vn.utc.hotelmanager.hotel.data.ReceiptRoomRepository;
import vn.utc.hotelmanager.hotel.data.RoomRepository;
import vn.utc.hotelmanager.hotel.data.dto.BookingRequestDTO;
import vn.utc.hotelmanager.hotel.model.Receipt;
import vn.utc.hotelmanager.hotel.model.ReceiptRoom;
import vn.utc.hotelmanager.hotel.model.Room;

import javax.transaction.Transactional;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Service
public class BookingService {

    private final UserRepository userRepository;
    private final RoomRepository roomRepository;
    private final ReceiptRepository receiptRepository;
    private final ReceiptRoomRepository receiptRoomRepository;

    @Autowired
    public BookingService(UserRepository userRepository, RoomRepository roomRepository, ReceiptRepository receiptRepository, ReceiptRoomRepository receiptRoomRepository) {
        this.userRepository = userRepository;
        this.roomRepository = roomRepository;
        this.receiptRepository = receiptRepository;
        this.receiptRoomRepository = receiptRoomRepository;
    }

    @Transactional
    public void BookThisRoom(BookingRequestDTO bookingRequest) {
        verifyBookingRequest(bookingRequest);
        Room targetRoom = roomRepository.findById(bookingRequest.getRoomId()).orElseThrow(
                () -> new ResourceNotFoundException(
                        String.format("Room with id %d does not exist", bookingRequest.getRoomId())
                )
        );

        if (targetRoom.getRoomType().getSeatCount() < bookingRequest.getCapacity())
            throw new InvalidRequestException("Too many people, this room does not fit");

        User bookingUser = userRepository.findByUsername(ApplicationUserService.getCurrentUser().getUsername());
        Receipt newReceipt = Receipt.builder()
                .created_date(Instant.now())
                .user(bookingUser)
                .build();
        receiptRepository.save(newReceipt);

        ReceiptRoom newReceiptRoom = ReceiptRoom.builder()
                .receipt(newReceipt)
                .room(targetRoom)
                .arrivalTime(bookingRequest.getStartDate().atZone(ZoneId.systemDefault()).toInstant())
                .leaveTime(bookingRequest.getEndDate().atZone(ZoneId.systemDefault()).toInstant())
                .build();

        try {
            receiptRoomRepository.save(newReceiptRoom);
        } catch (Exception e) {
            String msg = e.getMessage();
            if (e.getCause().getCause() instanceof SQLException)
                msg = e.getCause().getCause().getMessage();

            throw new RepositoryAccessException(
                    String.format("Cannot book this room: %s", msg));
        }
    }

    private void verifyBookingRequest(BookingRequestDTO bookingRequest) {
        if (bookingRequest.getRoomId() == null)
            throw new InvalidRequestException("Room id cannot be null");

        if (bookingRequest.getRoomId() < 0)
            throw new InvalidRequestException("Invalid room id: cannot be negative");

        LocalDateTime startDate = bookingRequest.getStartDate();
        LocalDateTime endDate = bookingRequest.getEndDate();

        if (startDate == null)
            throw new InvalidRequestException("Booking start date cannot be empty");

        if (endDate == null)
            throw new InvalidRequestException("Booking end date cannot be empty");

        if (startDate.isAfter(endDate))
            throw new InvalidRequestException("Booking start date cannot be behind end date");

        if (bookingRequest.getCapacity() == null)
            throw new InvalidRequestException("Capacity cannot be empty");

        if (bookingRequest.getCapacity() < 0)
            throw new InvalidRequestException("Invalid capacity size: cannot be less than zero");
    }
}
