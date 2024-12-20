package likelion13.page.exception;

public class NotEnoughCountException extends RuntimeException {
    public NotEnoughCountException() {
        super();
    }

    public NotEnoughCountException(String message) {
        super(message);
    }

    public NotEnoughCountException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotEnoughCountException(Throwable cause) {
        super(cause);
    }

    protected NotEnoughCountException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}