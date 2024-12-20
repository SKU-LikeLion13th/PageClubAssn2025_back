package likelion13.page.controller.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import likelion13.page.DTO.JoinEventDTO.RequestJoinEvent;
import likelion13.page.DTO.JoinEventDTO.RequestJoinEventForDelete;
import likelion13.page.DTO.JoinEventDTO.RequestMemberId;
import likelion13.page.DTO.JoinEventDTO.ResponseJoinEvent;
import likelion13.page.service.JoinEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static likelion13.page.DTO.EventDTO.ResponsePuzzleForNotPart;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/join-events")
@Tag(name = "관리자 페이지: 퍼즐조각 관리 관련")
public class JoinEventAdminController {
    public final JoinEventService joinEventService;

    // 회원 퍼즐 조각 관리 페이지
    @Operation(summary = "(호주) 회원의 이벤트 리스트 조회", description = "관리자 토큰 필요, 바디에 학번 필요")
    @PostMapping("")
    public ResponseEntity<List<ResponseJoinEvent>> manageEvents(@RequestBody RequestMemberId request) {
        List<ResponseJoinEvent> responsePuzzles = joinEventService.findAllJoinEvents(request.getStudentId());

        return ResponseEntity.ok(responsePuzzles);
    }

    // 회원 퍼즐 조각 삭제 페이지(기본키를 받아오나? 이벤트 이름만 받나?)
    @Operation(summary = "이벤트 참여 삭제", description = "관리자 토큰 필요, 바디에 학번 필요")
    @DeleteMapping("")
    public void deleteEvents(@RequestBody RequestJoinEventForDelete request) {
        joinEventService.removeJoinEvent(request.getStudentId(), request.getId());
    }

    // 회원 퍼즐 조각 추가 페이지(참여 안한거만 나오게)
    @Operation(summary = "(호주) 회원의 미참여 이벤트 리스트 조회", description = "관리자 토큰 필요, 파라미터에 학번 필요")
    @GetMapping("")
    public ResponseEntity<List<ResponsePuzzleForNotPart>> pageForAddEvent(@RequestParam String studentId) {
        List<ResponsePuzzleForNotPart> responseEvents = joinEventService.findNotPartEventsExceptImage(studentId);

        return ResponseEntity.ok(responseEvents);
    }

    // 회원 퍼즐 조각 추가 +
    @Operation(summary = "(호주) 이벤트 참여 추가", description = "관리자 토큰 필요, 바디에 학번 필요")
    @PostMapping("/add")
    public ResponseEntity<?> addJoinEvent(@RequestBody RequestJoinEvent request) {
        joinEventService.saveJoinEvent(request.getStudentId(), request.getId());

        return ResponseEntity.ok().build();
    }
}
