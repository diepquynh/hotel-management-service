package vn.utc.hotelmanager.auth.user.data.dto;

import lombok.Data;

@Data
public class UserRegistrationDTO {
    private String username;
    private String email;
    private String password;
}
