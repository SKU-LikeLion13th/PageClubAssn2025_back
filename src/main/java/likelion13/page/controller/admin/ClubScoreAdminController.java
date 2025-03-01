package likelion13.page.controller.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import likelion13.page.DTO.ClubScoreDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import likelion13.page.DTO.ClubScoreDTO.ClubScoreRequestDTO;
import likelion13.page.DTO.ClubScoreDTO.ClubScoreResponseDTO;
import likelion13.page.service.ClubScoreService;

import java.util.List;

@Tag(name = "관리자 페이지: 동아리 점수 관련")
@RestController
@RequestMapping("/admin/club-scores")
@RequiredArgsConstructor
public class ClubScoreAdminController {

    private final ClubScoreService clubScoreService;

    // 점수 저장
    @Operation(summary = "(준범)동아리 점수 추가 또는 업데이트",
            description = "body에 json 형태로 동아리 이름과 점수를 포함하여 전송하면, 기존 데이터가 있으면 업데이트, 없으면 새로운 데이터를 추가합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "정상적으로 저장/업데이트 완료"),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청 (예: 없는 동아리)")
            })
    @PostMapping("/add-or-update")
    public ResponseEntity<Void> saveOrUpdateScores(@RequestBody List<ClubScoreRequestDTO> requestDTO) {
        clubScoreService.saveOrUpdateScores(requestDTO);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Operation(summary = "(준범)특정 동아리의 점수 데이터 삭제 ",
            description = "club_id를 받아 해당 동아리의 점수 데이터를 삭제합니다. ****http://localhost:8080/admin/club-scores/delete/9 <= club_id는 요런식으로 넣어주면 됨****",
            responses = {
                    @ApiResponse(responseCode = "200", description = "정상적으로 삭제됨(데이터 없어도 에러안나게 다 통과시킬꺼임)")
            })
    @DeleteMapping("/delete/{clubId}")
    public ResponseEntity<Void> deleteClubScore(@PathVariable Long clubId) {
        clubScoreService.deleteClubScore(clubId);
        return ResponseEntity.ok().build();
    }

    //내림차순 점수 조회
    @Operation(summary = "모든 동아리 점수 조회(만약 관리자 페이지에서 조회하는거 사용한다면 이거 사용 권장)",
            description = "모든 동아리 점수를 점수순으로 정렬하여 반환합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "성공적으로 조회됨"),
                    @ApiResponse(responseCode = "404", description = "점수 데이터가 존재하지 않음")
            })
    @GetMapping("/all")
    public ResponseEntity<List<ClubScoreDTO.ClubScoreAdminResponseDTO>> getAllScores() {
        List<ClubScoreDTO.ClubScoreAdminResponseDTO> responseDTO = clubScoreService.getAllScores();
        return ResponseEntity.ok(responseDTO);
    }
}
