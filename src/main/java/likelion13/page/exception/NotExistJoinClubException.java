package likelion13.page.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

public class NotExistJoinClubException extends MessageException {

    public NotExistJoinClubException(String message, HttpStatus status) {
        super(message, status);
    }
}
