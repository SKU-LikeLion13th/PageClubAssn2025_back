package likelion13.page.controller.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import likelion13.page.DTO.MemberClubDTO.MemberJoinedClubDTO;
import likelion13.page.DTO.MemberClubDTO.MemberJoinedUnjoinedClubDTO;
import likelion13.page.DTO.MemberDTO;
import likelion13.page.service.JoinClubService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static likelion13.page.DTO.JoinClubDTO.CreateJC;
import static likelion13.page.DTO.JoinClubDTO.DeleteJC;
import static likelion13.page.DTO.MemberDTO.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/join-club")
@Tag(name = "관리자 페이지: 동아리 관리 관련")
public class JoinClubAdminController {
    private final JoinClubService joinClubService;

    @Operation(summary = "(민규) 동아리원 추가", description = "학번, 성함, 동아리명 필요")
    @PostMapping("/add")
    public ResponseEntity<?> addNewMember(@RequestBody CreateJC request) {
        joinClubService.saveNewMember(request.getStudentId(), request.getClubName());

        return ResponseEntity.status(HttpStatus.CREATED).build();
//        try {
//            JoinClub joinClub = joinClubService.saveNewMember(request.getStudentId(), request.getStudentName(), request.getClubName());
//            return ResponseEntity.status(HttpStatus.CREATED).body(joinClub);
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
//        }
    }

    @Operation(summary = "(민규) 동아리원 삭제", description = "학번, 동아리 id 필요")
    @DeleteMapping("")
    public void deleteClubMember(@RequestBody DeleteJC request) {
        joinClubService.deleteJoinClub(request.getMemberId(), request.getClubId());
    }

    @Operation(summary = "(민규) 동아리원 검색", description = "학번, 이름, 동아리를 검색하면 알맞은 동아리원 정보가 나옴")
    @GetMapping("/search")
    public ResponseEntity<List<CreateJC>> CMManageSearch(@RequestParam String keyword) {
//        if (keyword == null || keyword.trim().isEmpty()) {
//            return ResponseEntity.noContent().build();
//        }
        List<CreateJC> results = joinClubService.searchByKeyword(keyword);
        if(results.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(results);
        }
    }
//    @Operation(summary = "학생에게 동아리 부여(동아리 가입) API", description = "body에 동아리명, url에 학번 입력", tags={"joinclub", "add"})
//    @PostMapping("/club/add/{studentId}")
//    public ClubDTO.ResponseJoinClub addJoinClub(@RequestBody ClubDTO.RequestJoinClub request, @PathVariable("studentId") String studentId) {
//        joinClubService.saveNewMember(studentId, request.getClubName());
//        return new ClubDTO.ResponseJoinClub(studentId, request.getClubName());
//    }

    // 모든 멤버의 가입된 클럽 리스트
    @Operation(summary = "(택원) 모든 멤버의 가입된 동아리 리스트 반환 API", description = "")
    @GetMapping("/all-list")
    public ResponseEntity<List<MemberJoinedClubDTO>> findJoinedClubsForAllMember(){
        return ResponseEntity.ok().body(joinClubService.findJoinedClubsForAllMember());
    }

    // 특정 멤버의 가입 동아리, 미가입 동아리 리스트
    @Operation(summary = "(택원) 학생이 가입한 동아리와 가입하지 않은 동아리 반환 API", description = "")
    @PostMapping("/info")
    public ResponseEntity<MemberJoinedUnjoinedClubDTO> findJoinedClubUnJoinedClub(@RequestBody RequestMemberId request){
        return ResponseEntity.ok().body(joinClubService.findJoinedClubUnJoinedClub(request.getStudentId()));
    }
}
