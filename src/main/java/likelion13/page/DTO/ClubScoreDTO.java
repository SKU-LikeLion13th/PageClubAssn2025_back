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
        private int ranking;
        private int score;
        private String clubName;
        private byte[] logo;
    }

    @Data
    @AllArgsConstructor
    public static class ClubScoreAdminResponseDTO {
        private String quarter;
        private int score;
        private String clubName;
        private Long clubId;
    }
}
