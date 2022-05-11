package vn.utc.userservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT)
public class UserAlreadyExistException extends AuthenticationException {
    public UserAlreadyExistException(String msg) {
        super(msg);
    }
}
