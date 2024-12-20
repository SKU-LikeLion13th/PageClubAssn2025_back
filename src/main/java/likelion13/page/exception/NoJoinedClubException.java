package likelion13.page.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

public class NoJoinedClubException extends MessageException {

    public NoJoinedClubException(String message, HttpStatus status) {
        super(message, status);
    }
}
