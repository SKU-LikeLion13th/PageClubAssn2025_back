package likelion13.page.exception;

import org.springframework.http.HttpStatus;

public class DuplicatedRankingException extends RuntimeException {
    private final HttpStatus status;

    public DuplicatedRankingException(String message) {
        super(message);
        this.status = HttpStatus.BAD_REQUEST; // 400 에러로 설정
    }

    public HttpStatus getStatus() {
        return status;
    }
}