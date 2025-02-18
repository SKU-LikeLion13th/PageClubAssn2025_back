package likelion13.page.exception;

import lombok.Getter;

public class InvaildExcelFileException extends RuntimeException{
    @Getter
    private final int statusCode;
    public InvaildExcelFileException(int statusCode, String message) {
        super(message);
        this.statusCode = statusCode;
    }
}
