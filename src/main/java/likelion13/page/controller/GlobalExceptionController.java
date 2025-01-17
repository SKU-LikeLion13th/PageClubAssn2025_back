package likelion13.page.controller;

import likelion13.page.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionController {
    @ExceptionHandler(DelayedReturnException.class)
    public ResponseEntity<String> delayedReturn(Exception e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("반납 3회 지연으로 대여 서비스가 제한되었습니다.");
    }

    @ExceptionHandler(NotReturnException.class)
    public ResponseEntity<String> notReturn(Exception e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("미반납 1회로 대여 서비스가 제한되었습니다.");
    }

    @ExceptionHandler(LimitRentException.class)
    public ResponseEntity<String> limitRent(Exception e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
    }

    @ExceptionHandler(NotEnoughItemException.class)
    public ResponseEntity<String> notEnoughItem(Exception e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("물품의 잔여 개수가 부족합니다.");
    }

    @ExceptionHandler(MessageException.class)
    public ResponseEntity<String> runtimeException(MessageException e){
        return ResponseEntity.status(e.getStatus()).body(e.getMessage());
    }

    @ExceptionHandler(DuplicatedRankingException.class)
    public ResponseEntity<String> handleDuplicatedRankingException(DuplicatedRankingException e) {
        return ResponseEntity.status(e.getStatus()).body(e.getMessage());
    }

    @ExceptionHandler(ClubScoreNotFoundException.class)
    public ResponseEntity<String> handleClubScoreNotFoundException(ClubScoreNotFoundException e) {
        return ResponseEntity.status(e.getStatus()).body(e.getMessage());
    }

//    @ExceptionHandler(MemberExistException.class)
//    public ResponseEntity<String> alreadyExistMember(MemberExistException e) {
//        return ResponseEntity.status(e.getStatus()).body(e.getMessage());
//    }
//
//    @ExceptionHandler(MemberLoginException.class)
//    public ResponseEntity<String> handleMemberLoginException(MemberLoginException e) {
//        return ResponseEntity.status(e.getStatus()).body(e.getMessage());
//    }
//
//    @ExceptionHandler(NotExistJoinEventException.class)
//    public ResponseEntity<String> handleJoinEventException(NotExistJoinEventException e) {
//        return ResponseEntity.status(e.getStatus()).body(e.getMessage());
//    }
//
//    @ExceptionHandler(NotExistJoinClubException.class)
//    public ResponseEntity<String> handleJoinEventException(NotExistJoinClubException e) {
//        return ResponseEntity.status(e.getStatus()).body(e.getMessage());
//    }
//
//    @ExceptionHandler(CustomEventException.class)
//    public ResponseEntity<String> handleEventException(CustomEventException e) {
//        return ResponseEntity.status(e.getStatus()).body(e.getMessage());
//    }
//
//    @ExceptionHandler(NoJoinedClubException.class)
//    public ResponseEntity<String> handleNoJoinedClubException(NoJoinedClubException e) {
//        return ResponseEntity.status(e.getStatus()).body(e.getMessage());
//    }
//
//    @ExceptionHandler(ExistJoinClubException.class)
//    public ResponseEntity<String> handleExistJoinClubException(NoJoinedClubException e) {
//        return ResponseEntity.status(e.getStatus()).body(e.getMessage());
//    }
}