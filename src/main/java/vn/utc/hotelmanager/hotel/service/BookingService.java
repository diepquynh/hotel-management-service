package vn.utc.hotelmanager.hotel.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import vn.utc.hotelmanager.auth.user.data.UserRepository;
import vn.utc.hotelmanager.auth.user.model.User;
import vn.utc.hotelmanager.utils.ApplicationUserService;
import vn.utc.hotelmanager.exception.InvalidRequestException;
import vn.utc.hotelmanager.exception.RepositoryAccessException;
import vn.utc.hotelmanager.hotel.data.ReceiptRepository;
import vn.utc.hotelmanager.hotel.data.ReceiptRoomRepository;
import vn.utc.hotelmanager.hotel.data.RoomRepository;
import vn.utc.hotelmanager.hotel.data.BookingRepository;
import vn.utc.hotelmanager.hotel.data.dto.BookingDTO;
import vn.utc.hotelmanager.hotel.data.dto.BookingDetailsDTO;
import vn.utc.hotelmanager.hotel.data.dto.request.BookingUpdateRequestDTO;
import vn.utc.hotelmanager.hotel.model.Receipt;
import vn.utc.hotelmanager.hotel.model.ReceiptRoom;
import vn.utc.hotelmanager.hotel.model.Room;
import vn.utc.hotelmanager.hotel.model.Booking;
import vn.utc.hotelmanager.hotel.utils.PaymentStateConstants;
import vn.utc.hotelmanager.utils.DateUtils;

import javax.transaction.Transactional;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookingService {

    private final UserRepository userRepository;
    private final RoomRepository roomRepository;
    private final ReceiptRepository receiptRepository;
    private final ReceiptRoomRepository receiptRoomRepository;
    private final BookingRepository bookingRepository;

    @Autowired
    public BookingService(UserRepository userRepository, RoomRepository roomRepository,
                          ReceiptRepository receiptRepository, ReceiptRoomRepository receiptRoomRepository,
                          BookingRepository bookingRepository) {
        this.userRepository = userRepository;
        this.roomRepository = roomRepository;
        this.receiptRepository = receiptRepository;
        this.receiptRoomRepository = receiptRoomRepository;
        this.bookingRepository = bookingRepository;
    }

    public List<BookingDTO> getAllBookings() {
        return bookingRepository.findAll().stream().map(BookingDTO::new)
                .collect(Collectors.toList());
    }

    public List<BookingDTO> getUserBookings() {
        List<BookingDTO> bookingDTOS = new ArrayList<>();

        List<Booking> userReceipts = bookingRepository.findBookingsByUsername(
                ApplicationUserService.getCurrentUser().getUsername()
        );

        if (!CollectionUtils.isEmpty(userReceipts))
            bookingDTOS = userReceipts.stream().map(BookingDTO::new)
                    .collect(Collectors.toList());

        return bookingDTOS;
    }

    @Transactional
    public void BookThisRoom(BookingDTO bookingRequest) {
        if (CollectionUtils.isEmpty(bookingRequest.getBookingDetails()))
            throw new InvalidRequestException("Booking details cannot be empty");

        ArrayList<ReceiptRoom> receiptRooms = new ArrayList<>();
        double balance = 0;

        for (BookingDetailsDTO bookingDetail : bookingRequest.getBookingDetails()) {
            verifyBookingRequest(bookingDetail);
            Room targetRoom = roomRepository.findById(bookingDetail.getRoomId()).orElseThrow(
                    () -> new ResourceNotFoundException(
                            String.format("Room with id %d does not exist", bookingDetail.getRoomId())
                    )
            );

            if (targetRoom.getRoomType().getCapacity() < bookingDetail.getCapacity())
                throw new InvalidRequestException("Too many people, this room does not fit");

            Instant startDate = bookingDetail.getStartDate().atZone(ZoneId.systemDefault()).toInstant();
            Instant endDate = bookingDetail.getEndDate().atZone(ZoneId.systemDefault()).toInstant();

            int numDays = DateUtils.daysBetween(startDate, endDate);
            balance += targetRoom.getRoomType().getPrice() * numDays;

            ReceiptRoom newReceiptRoom = ReceiptRoom.builder()
                    .room(targetRoom)
                    .arrivalTime(startDate)
                    .leaveTime(endDate)
                    .capacity(bookingDetail.getCapacity())
                    .build();

            receiptRooms.add(newReceiptRoom);
        }

        try {
            User bookingUser = userRepository.findByUsername(ApplicationUserService.getCurrentUser().getUsername());
            Receipt newReceipt = Receipt.builder()
                    .created_date(Instant.now())
                    .total_balance(balance)
                    .build();
            receiptRepository.save(newReceipt);

            Booking booking = Booking.builder()
                    .user(bookingUser)
                    .receipt(newReceipt)
                    .arrived(false)
                    .paymentState(PaymentStateConstants.UNPAID.getValue())
                    .build();
            bookingRepository.save(booking);

            for (ReceiptRoom receiptRoom : receiptRooms)
                receiptRoom.setReceipt(newReceipt);

            receiptRoomRepository.saveAll(receiptRooms);
        } catch (Exception e) {
            String msg = e.getMessage();
            if (e.getCause().getCause() instanceof SQLException)
                msg = e.getCause().getCause().getMessage();

            throw new RepositoryAccessException(
                    String.format("Cannot book this room: %s", msg));
        }
    }

    public void guestHasArrived(BookingUpdateRequestDTO updateRequest) {
        Booking thisBooking = bookingRepository.findByReceiptId(
                updateRequest.getReceiptId()
        ).orElseThrow(
                () -> new ResourceNotFoundException(
                        String.format("User receipt with id %d not found",
                                updateRequest.getReceiptId())
                )
        );

        if (!ApplicationUserService.currentUserHasAdminRole()) {
            if (thisBooking.getUser().getId() != updateRequest.getUserId())
                throw new InvalidRequestException(
                        String.format("Receipt with id %d does not belong to user with id %d",
                                updateRequest.getReceiptId(), updateRequest.getUserId())
                );
        }

        thisBooking.setArrived(true);

        try {
            bookingRepository.save(thisBooking);
        } catch (Exception e) {
            throw new RepositoryAccessException(
                    String.format("Cannot set arrived state: %s", e.getMessage()));
        }
    }

    private void verifyBookingRequest(BookingDetailsDTO bookingDetail) {
        LocalDateTime startDate = bookingDetail.getStartDate();
        LocalDateTime endDate = bookingDetail.getEndDate();

        if (startDate.isAfter(endDate))
            throw new InvalidRequestException("Booking start date cannot be behind end date");
    }
}
