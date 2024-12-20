package likelion13.page.exception;

import org.springframework.http.HttpStatus;

public class ExistJoinEventException extends MessageException{
    public ExistJoinEventException(String message, HttpStatus status) {
        super(message, status);
    }
}
