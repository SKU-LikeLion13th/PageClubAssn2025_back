package likelion13.page.exception;

import org.springframework.http.HttpStatus;

public class NotExistClubException extends MessageException {

    public NotExistClubException(String message, HttpStatus status) {
        super(message, status);
    }
}
