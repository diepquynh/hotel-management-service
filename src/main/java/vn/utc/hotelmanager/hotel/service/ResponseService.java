package vn.utc.hotelmanager.hotel.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.utc.hotelmanager.exception.InvalidRequestException;
import vn.utc.hotelmanager.exception.RepositoryAccessException;
import vn.utc.hotelmanager.auth.user.data.UserRepository;
import vn.utc.hotelmanager.auth.user.model.User;
import vn.utc.hotelmanager.utils.ApplicationUserService;
import vn.utc.hotelmanager.hotel.data.ResponseRepository;
import vn.utc.hotelmanager.hotel.data.dto.response.HotelResponseDTO;
import vn.utc.hotelmanager.hotel.model.Response;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ResponseService {

    private final ResponseRepository responseRepository;
    private final UserRepository userRepository;

    @Autowired
    public ResponseService(ResponseRepository responseRepository, UserRepository userRepository) {
        this.responseRepository = responseRepository;
        this.userRepository = userRepository;
    }

    public List<HotelResponseDTO> getAllUserResponses() {
        return responseRepository.findAll()
                .stream().map(HotelResponseDTO::new)
                .collect(Collectors.toList());
    }

    public List<HotelResponseDTO> getUserResponses() {
        String currentUsername = ApplicationUserService.getCurrentUser().getUsername();
        return responseRepository.findAllByUsername(currentUsername)
                .stream().map(HotelResponseDTO::new)
                .collect(Collectors.toList());
    }

    public void createUserResponse(String content) {
        if (content == null || content.trim().isEmpty())
            throw new InvalidRequestException("Response content cannot be empty!");

        String currentUsername = ApplicationUserService.getCurrentUser().getUsername();
        User currentUser = userRepository.findByUsername(currentUsername);

        Response newResponse = Response.builder()
                .content(content)
                .user(currentUser)
                .build();

        try {
            responseRepository.save(newResponse);
        } catch (Exception e) {
            throw new RepositoryAccessException("Cannot save this response: " + e.getMessage());
        }
    }
}
