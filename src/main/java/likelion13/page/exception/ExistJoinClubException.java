package likelion13.page.exception;

import org.springframework.http.HttpStatus;

public class ExistJoinClubException extends MessageException {

    public ExistJoinClubException(String message, HttpStatus status) {
        super(message, status);
    }
}
