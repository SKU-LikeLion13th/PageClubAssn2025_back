package likelion13.page.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

public class ClubScoreDTO {

    @Data
    public static class ClubScoreRequestDTO {
        private String quarter; // 분기 (예: "2023-Q1")
        private int ranking;       // 순위 (1, 2, 3)
        private String clubName; // 동아리명
        private int score;      // 점수
    }

    @Data
    public static class quarterRequestDTO {
        private String quarter; // 분기 (예: "2023-Q1")
    }
    @Data
    @AllArgsConstructor
    public static class ClubScoreResponseDTO {
        private String quarter; // 분기 (예: "2023-Q1")
        private int ranking;       // 순위 (1, 2, 3)
        private int score;
        private String clubName;
        private byte[] logo;
    }

    @Data
    public static class ClubScoreUpdateRequestDTO {
        private String quarter; // 분기
        private int ranking;    // 순위
        private String clubName; // 새 동아리명
        private int score;      // 새 점수
    }
}
