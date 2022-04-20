package vn.utc.hotelmanager.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
public class RepositoryAccessException extends RuntimeException {
    public RepositoryAccessException(String msg) {
        super(msg);
    }
}
