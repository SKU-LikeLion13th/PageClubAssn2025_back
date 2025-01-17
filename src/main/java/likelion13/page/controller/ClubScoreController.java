package likelion13.page.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import likelion13.page.DTO.ClubScoreDTO;
import likelion13.page.service.ClubScoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "멤버 페이지: 동아리 점수")
@RestController
@RequestMapping("/club-scores")
@RequiredArgsConstructor
public class ClubScoreController {

    private final ClubScoreService clubScoreService;


    // 특정 분기의 상위 3개 점수 조회
    @Operation(summary = "(준범)전체 점수 조회",
            description = "순위 기준으로 정렬된 모든 점수를 조회.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "성공적으로 조회됨"),
                    @ApiResponse(responseCode = "404", description = "점수 데이터가 존재하지 않음")
            })
    @GetMapping("/all")
    public ResponseEntity<List<ClubScoreDTO.ClubScoreResponseDTO>> getAllScores() {
        List<ClubScoreDTO.ClubScoreResponseDTO> responseDTO = clubScoreService.getAllScores();
        return ResponseEntity.ok(responseDTO);

    }
}
