package vn.utc.hotelmanager.jwt;

import lombok.Builder;
import lombok.Data;
import vn.utc.hotelmanager.auth.user.model.User;

@Data
@Builder
public class SuccessfulAuthenticationResponse {
    private String token;
    private User user;
}
