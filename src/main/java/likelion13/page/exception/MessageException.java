package likelion13.page.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class MessageException extends RuntimeException{
    private HttpStatus status;
    private String message;

    public MessageException(String message, HttpStatus status) {
        super(message);
        this.message = message;
        this.status = status;
    }
}
