package vn.utc.hotelmanager.hotel.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import vn.utc.hotelmanager.exception.InvalidRequestException;
import vn.utc.hotelmanager.exception.RepositoryAccessException;
import vn.utc.hotelmanager.exception.ResourceAlreadyExistedException;
import vn.utc.hotelmanager.hotel.data.*;
import vn.utc.hotelmanager.hotel.data.dto.request.HotelServiceFilterRequestDTO;
import vn.utc.hotelmanager.hotel.data.dto.request.HotelServiceRequestDTO;
import vn.utc.hotelmanager.hotel.data.dto.HotelServiceItemDTO;
import vn.utc.hotelmanager.hotel.data.dto.request.ServiceRequestDTO;
import vn.utc.hotelmanager.hotel.model.*;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ExtrasService {

    private final RoomRepository roomRepository;
    private final ServiceRepository serviceRepository;
    private final ReceiptRepository receiptRepository;
    private final ReceiptRoomRepository receiptRoomRepository;
    private final ReceiptRoomServiceRepository receiptRoomServiceRepository;

    @Autowired
    public ExtrasService(RoomRepository roomRepository, ServiceRepository serviceRepository,
                         ReceiptRepository receiptRepository, ReceiptRoomRepository receiptRoomRepository,
                         ReceiptRoomServiceRepository receiptRoomServiceRepository) {
        this.roomRepository = roomRepository;
        this.serviceRepository = serviceRepository;
        this.receiptRepository = receiptRepository;
        this.receiptRoomRepository = receiptRoomRepository;
        this.receiptRoomServiceRepository = receiptRoomServiceRepository;
    }

    public List<HotelServiceItemDTO> getAllHotelServices() {
        return serviceRepository.findAll()
                .stream().map(HotelServiceItemDTO::new)
                .collect(Collectors.toList());
    }

    public List<HotelServiceItemDTO> getFilteredHotelServices(
            HotelServiceFilterRequestDTO serviceRequest) {
        verifyFilterRequest(serviceRequest);

        return serviceRepository.findFilteredServices(serviceRequest)
                .stream().map(HotelServiceItemDTO::new)
                .collect(Collectors.toList());
    }

    public HotelServiceItemDTO getHotelService(Integer id) {
        return serviceRepository.findById(id)
                .map(HotelServiceItemDTO::new)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Hotel service with id %d not found", id)
                ));
    }

    public void addService(HotelServiceItemDTO serviceItem) {
        verifyServiceItemRequest(serviceItem);
        Optional.ofNullable(
                serviceRepository.findByName(serviceItem.getName().trim())
        ).orElseThrow(
                () -> new ResourceAlreadyExistedException(
                        String.format("Service with name %s already existed",
                                serviceItem.getName())
                )
        );

        vn.utc.hotelmanager.hotel.model.Service service =
                vn.utc.hotelmanager.hotel.model.Service.builder()
                        .name(serviceItem.getName())
                        .price(serviceItem.getPrice())
                        .type(serviceItem.getType())
                        .image(serviceItem.getImage())
                        .build();

        try {
            serviceRepository.save(service);
        } catch (Exception e) {
            throw new RepositoryAccessException(
                    String.format("Unable to add service: %s", e.getMessage())
            );
        }
    }

    public void updateService(HotelServiceItemDTO serviceItem) {
        verifyServiceItemRequest(serviceItem);
        if (serviceItem.getId() == null)
            throw new InvalidRequestException("Service id cannot be null!");

        if (serviceItem.getId() < 0)
            throw new InvalidRequestException("Invalid service id: cannot be less than zero!");

        vn.utc.hotelmanager.hotel.model.Service targetService =
                serviceRepository.findById(serviceItem.getId())
                        .orElseThrow(
                                () -> new ResourceNotFoundException(
                                        String.format("Hotel service with id %d not found",
                                                serviceItem.getId())
                                )
                        );

        targetService.setName(serviceItem.getName());
        targetService.setPrice(serviceItem.getPrice());
        targetService.setType(serviceItem.getType());
        targetService.setImage(serviceItem.getImage());

        try {
            serviceRepository.save(targetService);
        } catch (Exception e) {
            throw new RepositoryAccessException(
                    String.format("Unable to update service with id %d: %s",
                            targetService.getId(), e.getMessage())
            );
        }
    }

    public void deleteService(Integer serviceId) {
        vn.utc.hotelmanager.hotel.model.Service targetService =
                serviceRepository.findById(serviceId)
                        .orElseThrow(
                                () -> new ResourceNotFoundException(
                                        String.format("Hotel service with id %d not found",
                                                serviceId)
                                )
                        );

        try {
            serviceRepository.delete(targetService);
        } catch (Exception e) {
            throw new RepositoryAccessException(
                    String.format("Unable to delete service with id %d: %s",
                            targetService.getId(), e.getMessage())
            );
        }
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
        Booking targetBooking = targetReceipt.getBooking();
        if (targetBooking.getArrived().equals(false))
            throw new InvalidRequestException(
                    String.format("Room with id %d has no guest arrived",
                            serviceRequest.getRoomId())
            );

        Set<ReceiptRoomService> receiptRoomServices = new HashSet<>();
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

            ReceiptRoomService targetReceiptRoomService =
                    ReceiptRoomService.builder()
                            .receiptRoom(targetReceiptRoom)
                            .service(targetService)
                            .quantity(request.getQuantity())
                            .build();

            receiptRoomServices.add(targetReceiptRoomService);
            targetReceipt.addTotalBalance(
                    targetService.getPrice() * request.getQuantity());
        }

        try {
            receiptRoomServiceRepository.saveAll(receiptRoomServices);
            targetReceiptRoom.setReceiptRoomServices(receiptRoomServices);
            receiptRoomRepository.save(targetReceiptRoom);
            receiptRepository.save(targetReceipt);
        } catch (Exception e) {
            throw new RepositoryAccessException(
                    String.format("Unable to request services for room id %d: %s",
                            serviceRequest.getRoomId(), e.getMessage())
            );
        }
    }

    private void verifyServiceItemRequest(HotelServiceItemDTO serviceItem) {
        if (serviceItem.getName() == null)
            throw new InvalidRequestException("Service name cannot be null!");

        if (serviceItem.getPrice() == null)
            throw new InvalidRequestException("Service price cannot be null!");

        if (serviceItem.getPrice() < 0)
            throw new InvalidRequestException("Invalid service price: cannot be less than zero!");

        if (serviceItem.getType() == null)
            throw new InvalidRequestException("Service type cannot be null!");
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
