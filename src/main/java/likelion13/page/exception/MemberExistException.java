package likelion13.page.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

public class MemberExistException extends MessageException {

    public MemberExistException(String message, HttpStatus status) {
        super(message, status);
    }
}