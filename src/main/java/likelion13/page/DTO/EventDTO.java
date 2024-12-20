package likelion13.page.DTO;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.cglib.core.Local;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class EventDTO {


    @Data
    public static class ResponseEvent {
        @Schema(description = "이벤트 번호", example = "1")
        private Long id;

        @Schema(description = "이벤트 이름", example = "동아리페스티벌")
        private String name;

        @Schema(description = "이벤트 일자", example = "2024-04-06T11:14:51.57", type = "string")
        private LocalDate date;

        @Schema(description = "이벤트 포스터", example = "")
        private String image;

        public ResponseEvent(Long id, String name, String image, LocalDate date) {
            this.id = id;
            this.name = name;
            this.image = image;
            this.date = date;
        }
    }

    @Data
    public static class RequestEvent {
        @Schema(description = "이벤트 이름", example = "동아리페스티벌")
        private String name;
        @Schema(description = "이벤트 일자", example = "2024-04-06", type = "string")
        private LocalDate date;
        @Schema(description = "이벤트 이미지", example = "")
        private MultipartFile image;
    }

    @Data
    public static class UpdateRequestEvent {
        @Schema(description = "이벤트 번호", example = "1")
        private Long id;
        @Schema(description = "이벤트 이름", example = "동아리페스티벌")
        private String name;
        @Schema(description = "이벤트 일자", example = "2024-04-06", type = "string")
        private LocalDate date;
        @Nullable
        @Schema(description = "이벤트 이미지(생략가능)", example = "")
        private MultipartFile image;
    }

    @Data
    @AllArgsConstructor
    public static class UpdateResponseEvent {
        @Schema(description = "이벤트 이름", example = "동아리페스티벌")
        private String name;
        @Schema(description = "이벤트 일자", example = "2024-04-06T11:14:51.572", type = "string")
        private LocalDateTime date;
        @Schema(description = "이벤트 이미지", example = "")
        private String image;
    }

    @Data
    public static class ResponsePuzzleForNotPart {
        @Schema(description = "이벤트 번호", example = "1")
        private Long id;

        @Schema(description = "이벤트 이름", example = "동아리페스티벌")
        private String name;

        public ResponsePuzzleForNotPart(Long id, String name) {
            this.id = id;
            this.name = name;
        }
    }

    @Data
    @AllArgsConstructor
    public static class EventAllRequestExceptImage {
        @Schema(description = "이벤트 번호", example = "1")
        private Long id;

        @Schema(description = "이벤트 이름", example = "동아리페스티벌")
        private String name;

        @Schema(description = "이벤트 일자", example = "2024-04-06T11:14:51.572", type = "string")
        private LocalDate date;
    }

    @Data
    public static class AllEvents{
        @Schema(description = "이벤트 번호", example = "1")
        private Long id;

        @Schema(description = "이벤트 이름", example = "동아리페스티벌")
        private String name;

        @Schema(description = "이벤트 이미지", example = "")
        private String image;

        @Schema(description = "이벤트 일자", example = "2024-04-06T11:14:51.572", type = "string")
        private LocalDate date;

        @Schema(description = "참여여부", example = "true")
        private boolean joined;

        public AllEvents(Long id, String name, String image, LocalDate date, boolean joined) {
            this.id = id;
            this.name = name;
            this.image = image;
            this.date = date;
            this.joined = joined;
        }
    }

    @Data
    public static class DeleteEvents {
        @Schema(description = "이벤트 번호", example = "1")
        private Long id;
    }
}
