package likelion13.page.exception;

import org.springframework.http.HttpStatus;

public class CustomEventException extends MessageException {

    public CustomEventException(String message, HttpStatus status) {
        super(message, status);
    }
}
