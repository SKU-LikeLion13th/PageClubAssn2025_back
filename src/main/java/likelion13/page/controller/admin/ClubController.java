package likelion13.page.controller.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import likelion13.page.DTO.ClubDTO.ClubCreateRequest;
import likelion13.page.DTO.ClubDTO.ClubUpdateRequest;
import likelion13.page.DTO.ClubDTO.ClubUpdateResponse;
import likelion13.page.DTO.ClubDTO.RequestJoinClub;
import likelion13.page.domain.Club;
import likelion13.page.service.ClubService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/club")
@Tag(name = "관리자 페이지: 동아리 관리 관련")
public class ClubController {
    private final ClubService clubService;

//    @PostMapping("/club/add")
//    public Club testClub(@RequestBody Club club) {
//        return clubService.addNewClub(club.getName(), club.getDescription(), club.getLogo());
//    }

//    @PostMapping("/club/add/{studentId}")
//    public ResponseJoinClub addJoinClub(@RequestBody RequestJoinClub request, @PathVariable("studentId") String studentId) {
//        Club club = clubService.findByName(request.getClubName());
//        joinClubService.saveNewMember(studentId, club);
//
//        return new ResponseJoinClub(studentId, club.getName());
//    }

    // 동아리 추가
    @Operation(summary = "(민규) 동아리 추가", description = "동아리명과 동아리 설명, 로고 사진 필요",
            responses = {@ApiResponse(responseCode = "200", description = "생성"),
                    @ApiResponse(responseCode = "", description = "")})
    @PostMapping("/add") //, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Club> addClub(ClubCreateRequest request)  throws IOException {
        Club club = clubService.addNewClub(request.getClubName(), request.getDescription(), request.getLogo());
        return ResponseEntity.status(HttpStatus.CREATED).body(club);
    }

    // 동아리 수정
    @Operation(summary = "(민규) 동아리 수정", description = "동아리 id, 동아리명, 동아리 설명 필요, 이미지는 안바꾸고 싶으면 안넣으면 됨",
            responses = {@ApiResponse(responseCode = "201", description = "수정 성공 후 변경된 정보를 포함한 객체 생성 "),
                    @ApiResponse(responseCode = "", description = "")})
    @PutMapping("")
    public ResponseEntity<ClubUpdateResponse> changeClub(ClubUpdateRequest request) throws IOException {
        Club club = clubService.changeClub(request.getId(), request.getName(), request.getDescription(), request.getLogo());
        return ResponseEntity.status(HttpStatus.CREATED).body(new ClubUpdateResponse(club.getName(), club.getDescription(), club.arrayToImage()));
    }

//    @Operation(summary = "", description = "", tags = {"club"},
//            responses = {@ApiResponse(responseCode = "200", description = ""),
//                    @ApiResponse(responseCode = "", description = "")})
//    @GetMapping("club/{clubId}")
//    public ResponseEntity<ClubCreateRequest> findOneClub(@PathVariable("clubId") Long clubId) {
//        Club club = clubService.findById(clubId);
//        return ResponseEntity.status(HttpStatus.OK).body(new ClubCreateRequest(club.getName(), club.getDescription(), club.arrayToImage()));
//    }


    @Operation(summary = "(민규) 모든 동아리 조회", description = "모든 동아리에 대한 정보 조회",
            responses = {@ApiResponse(responseCode = "200", description = "조회를 하면 동아리의 아이디, 동아리명, 동아리 설명, 로고들을 묶은 리스트가 출력."),
                    @ApiResponse(responseCode = "", description = "")})
    @GetMapping("/all")
    public ResponseEntity<List<Club>> findAllClubs() {//프론트측 요청으로 그냥 Club 객체 그대로 뽑기로 결정
        List<Club> clubs = clubService.findAll();

        return ResponseEntity.status(HttpStatus.OK).body(clubs);
    }

    @Operation(summary = "동아리 삭제", description = "동아리 이름 넣으면 해당 동아리 삭제",
            responses = {@ApiResponse(responseCode = "200", description = "동아리 삭제 성공.")})
    @DeleteMapping("")
    public boolean deleteClub(@RequestBody RequestJoinClub request) {
        return clubService.deleteClub(request.getClubName());
    }
}