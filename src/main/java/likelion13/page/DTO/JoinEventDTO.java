package likelion13.page.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

public class JoinEventDTO {
    @Data
    public static class ResponseJoinEvent {
        @Schema(description = "행사 번호", example = "1")
        private Long id;

        @Schema(description = "행사명", example = "동아리페스티벌")
        private String name;

        @Schema(description = "행사 참여 시간", example = "2024-03-31T17:04:43.982361", type = "string")
        private LocalDateTime addedTime;

        public ResponseJoinEvent(Long id, String name, LocalDateTime dateTime) {
            this.id = id;
            this.name = name;
            this.addedTime = dateTime;
        }
    }

    @Data
    public static class RequestJoinEvent {
        @Schema(description = "학번", example = "00000000")
        private String studentId;

        @Schema(description = "행사 번호", example = "1")
        private Long id;
//        private String name;
//        private LocalDateTime addDate;
    }

    @Data
    public static class RequestJoinEventForDelete {
        @Schema(description = "학번", example = "00000000")
        private String studentId;

        @Schema(description = "행사 번호", example = "1")
        private Long id;

//        @Schema(description = "이벤트 이름", example = "동아리페스티벌")
//        private String name;
    }

    @Data
    public static class RequestMemberId {
        @Schema(description = "조회학번", example = "00000000")
        private String studentId;
    }
}
