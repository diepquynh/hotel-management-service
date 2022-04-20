package vn.utc.hotelmanager.auth.exception;

import org.springframework.security.core.AuthenticationException;

public class UserAlreadyExistException extends AuthenticationException {
    public UserAlreadyExistException(String msg) {
        super(msg);
    }
}
