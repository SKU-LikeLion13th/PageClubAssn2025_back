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

    // 전체 점수 조회 (순위 포함)
    @Operation(summary = "전체 동아리 점수 순위 조회",
            description = "점수를 기준으로 순위를 계산하여 반환합니다.(1~3등까지만)",
            responses = {
                    @ApiResponse(responseCode = "200", description = "성공적으로 조회됨"),
                    @ApiResponse(responseCode = "404", description = "점수 데이터가 존재하지 않음")
            })
    @GetMapping("/ranked")
    public ResponseEntity<List<ClubScoreDTO.ClubScoreResponseDTO>> getRankedScores() {
        List<ClubScoreDTO.ClubScoreResponseDTO> responseDTO = clubScoreService.getRankedScores();
        return ResponseEntity.ok(responseDTO);
    }
}
