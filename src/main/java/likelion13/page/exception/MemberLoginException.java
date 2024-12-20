package likelion13.page.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

public class MemberLoginException extends MessageException {

    public MemberLoginException(String message, HttpStatus status) {
        super(message, status);
    }
}