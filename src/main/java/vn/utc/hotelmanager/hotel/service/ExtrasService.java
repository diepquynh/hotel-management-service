package vn.utc.hotelmanager.hotel.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import vn.utc.hotelmanager.exception.InvalidRequestException;
import vn.utc.hotelmanager.exception.RepositoryAccessException;
import vn.utc.hotelmanager.hotel.data.ReceiptRepository;
import vn.utc.hotelmanager.hotel.data.ReceiptRoomRepository;
import vn.utc.hotelmanager.hotel.data.RoomRepository;
import vn.utc.hotelmanager.hotel.data.ServiceRepository;
import vn.utc.hotelmanager.hotel.data.dto.HotelServiceFilterRequestDTO;
import vn.utc.hotelmanager.hotel.data.dto.HotelServiceRequestDTO;
import vn.utc.hotelmanager.hotel.data.dto.HotelServiceResponseDTO;
import vn.utc.hotelmanager.hotel.data.dto.ServiceRequestDTO;
import vn.utc.hotelmanager.hotel.model.Receipt;
import vn.utc.hotelmanager.hotel.model.ReceiptRoom;
import vn.utc.hotelmanager.hotel.model.Room;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExtrasService {

    private final RoomRepository roomRepository;
    private final ServiceRepository serviceRepository;
    private final ReceiptRepository receiptRepository;
    private final ReceiptRoomRepository receiptRoomRepository;

    @Autowired
    public ExtrasService(RoomRepository roomRepository, ServiceRepository serviceRepository, ReceiptRepository receiptRepository, ReceiptRoomRepository receiptRoomRepository) {
        this.roomRepository = roomRepository;
        this.serviceRepository = serviceRepository;
        this.receiptRepository = receiptRepository;
        this.receiptRoomRepository = receiptRoomRepository;
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

    @Transactional
    public void requestServiceForRoom(HotelServiceRequestDTO serviceRequest) {
        if (serviceRequest.getRoomId() < 0)
            throw new InvalidRequestException("Invalid room id: cannot be less than zero!");

        if (CollectionUtils.isEmpty(serviceRequest.getServiceRequests()))
            throw new InvalidRequestException("Service requests cannot be empty!");

        Room targetRoom = roomRepository.findById(serviceRequest.getRoomId()).orElseThrow(
                () -> new ResourceNotFoundException(
                        String.format("Room with id %d not found", serviceRequest.getRoomId())
                ));

        ReceiptRoom targetReceiptRoom = targetRoom.getActiveReceiptRoom();
        if (targetReceiptRoom == null)
            throw new InvalidRequestException(
                    String.format("Room with id %d has not been booked. Please book this room first",
                            serviceRequest.getRoomId())
            );

        Receipt targetReceipt = targetReceiptRoom.getReceipt();
        for (ServiceRequestDTO request : serviceRequest.getServiceRequests()) {
            vn.utc.hotelmanager.hotel.model.Service targetService =
                    serviceRepository.findById(request.getServiceId()).orElseThrow(
                            () -> new ResourceNotFoundException(
                                    String.format("Hotel service with id %d not found",
                                            request.getServiceId())
                            ));

            if (request.getQuantity() < 0)
                throw new InvalidRequestException(
                        String.format("Invalid quantity %d: cannot be less than zero",
                                request.getQuantity())
                );

            targetReceiptRoom.addService(targetService);
            targetReceipt.addTotalBalance(
                    targetService.getPrice() * request.getQuantity());
        }

        try {
            receiptRoomRepository.save(targetReceiptRoom);
            receiptRepository.save(targetReceipt);
        } catch (Exception e) {
            throw new RepositoryAccessException(
                    String.format("Unable to request services for room id %d: %s",
                            serviceRequest.getRoomId(), e.getMessage())
            );
        }
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
