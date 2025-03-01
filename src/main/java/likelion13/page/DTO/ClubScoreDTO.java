package likelion13.page.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

public class ClubScoreDTO {

    @Data
    public static class ClubScoreRequestDTO {
        private String quarter;
        private String clubName;
        private int score;
    }

    @Data
    @AllArgsConstructor
    public static class ClubScoreResponseDTO {
        private String quarter;
        private int ranking; // 백엔드에서 계산 후 반환
        private int score;
        private String clubName;
        private byte[] logo;
    }
}
