package likelion13.page.exception;

public class DuplicatedStudentIdException extends RuntimeException{
    public DuplicatedStudentIdException(String message) {
        super(message);
    }
}
