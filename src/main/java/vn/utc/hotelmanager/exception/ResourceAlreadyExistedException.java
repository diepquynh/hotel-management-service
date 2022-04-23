package vn.utc.hotelmanager.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT)
public class ResourceAlreadyExistedException extends RuntimeException {
    public ResourceAlreadyExistedException(String msg) {
        super(msg);
    }
}
