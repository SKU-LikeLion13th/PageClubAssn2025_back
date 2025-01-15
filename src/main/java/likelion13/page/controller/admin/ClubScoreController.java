package likelion13.page.controller.admin;

import likelion13.page.DTO.ClubScoreDTO.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import likelion13.page.DTO.ClubScoreDTO.ClubScoreRequestDTO;
import likelion13.page.DTO.ClubScoreDTO.ClubScoreResponseDTO;
import likelion13.page.service.ClubScoreService;

import java.util.List;

@RestController
@RequestMapping("/admin/club-scores")
@RequiredArgsConstructor
public class ClubScoreController {

    private final ClubScoreService clubScoreService;

    // 점수 저장
    @PostMapping("/add-or-update")
    public ResponseEntity<Void> saveOrUpdateScores(@RequestBody List<ClubScoreRequestDTO> requestDTO) {
        clubScoreService.saveOrUpdateScores(requestDTO);
        return ResponseEntity.ok().build();
    }

    // 특정 분기의 상위 3개 점수 조회
    @GetMapping("/quarter")
    public ResponseEntity<List<ClubScoreResponseDTO>> getTop3ScoresByQuarter(@RequestBody quarterRequestDTO requestDTO) {
        List<ClubScoreResponseDTO> responseDTO = clubScoreService.getTop3ScoresByQuarter(requestDTO);
        return ResponseEntity.ok(responseDTO);
    }
}
