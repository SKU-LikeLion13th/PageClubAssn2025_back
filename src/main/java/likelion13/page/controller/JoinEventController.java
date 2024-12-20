package likelion13.page.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import likelion13.page.DTO.EventDTO.AllEvents;
import likelion13.page.security.JwtUtility;
import likelion13.page.service.JoinEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "멤버 페이지: 퍼즐조각 관련")
public class JoinEventController {
    private final JoinEventService joinEventService;
    private final JwtUtility jwtUtility;

    //멤버가 자신의 이벤트 조회(해당 멤버가 참여 했는지 여부를 포함한 모든 이벤트 조회)
    @Operation(summary = "(택원) 이벤트 조회", description = "request: jwt<br>response: 퍼즐조각들(id, 퍼즐 이름, 퍼즐 이미지, 일자, 참석여부)",
            responses = {@ApiResponse(responseCode = "200", description = "생성"),
                    @ApiResponse(responseCode = "", description = "")})
    @PostMapping("/puzzle")
    public ResponseEntity<List<AllEvents>> findEventsInfo(HttpServletRequest header){
        String studentId = jwtUtility.getStudentId(jwtUtility.resolveToken(header));
        return ResponseEntity.ok().body(joinEventService.findEventsInfo(studentId));
    }
}
