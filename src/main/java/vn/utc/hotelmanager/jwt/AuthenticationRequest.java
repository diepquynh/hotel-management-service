package vn.utc.hotelmanager.jwt;

import lombok.Data;

@Data
public class AuthenticationRequest {
    private String username;
    private String password;
}
