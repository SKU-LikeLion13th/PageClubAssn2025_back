package likelion13.page.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

public class ClubScoreDTO {

    @Data
    public static class ClubScoreRequestDTO {
        private String quarter;
        private int ranking;
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

//    @Data
//    public static class ClubScoreUpdateRequestDTO {
//        private String quarter;
//        private int ranking;
//        private String clubName;
//        private int score;
//    }
//
//    @Data
//    public static class quarterRequestDTO {
//        private String quarter;
//    }
}
