package likelion13.page.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

public class NotExistJoinEventException extends MessageException {

    public NotExistJoinEventException(String message, HttpStatus status) {
        super(message, status);
    }
}
