package likelion13.page.exception;

import org.springframework.http.HttpStatus;

public class ClubScoreNotFoundException extends RuntimeException {
    private final HttpStatus status;

    public ClubScoreNotFoundException(String message) {
        super(message);
        this.status = HttpStatus.NOT_FOUND; // 404 에러로 설정
    }

    public HttpStatus getStatus() {
        return status;
    }
}
