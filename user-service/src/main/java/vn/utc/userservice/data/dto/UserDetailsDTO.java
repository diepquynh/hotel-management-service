package vn.utc.userservice.data.dto;

import lombok.Data;
import vn.utc.userservice.model.User;

@Data
public class UserDetailsDTO {
    private Integer id;
    private String email;
    private String username;
    private UserProfileDetailsDTO profileDetails;

    public UserDetailsDTO(User user) {
        setId(user.getId());
        setEmail(user.getEmail());
        setUsername(user.getUsername());
        setProfileDetails(new UserProfileDetailsDTO(user.getProfile()));
    }
}
