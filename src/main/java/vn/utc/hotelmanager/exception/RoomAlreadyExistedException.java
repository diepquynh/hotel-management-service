package vn.utc.hotelmanager.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT)
public class RoomAlreadyExistedException extends RuntimeException {
    public RoomAlreadyExistedException(String msg) {
        super(msg);
    }
}
