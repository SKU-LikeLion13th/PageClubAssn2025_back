package likelion13.page.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;


public class ItemDTO {
    @Data
    @AllArgsConstructor
    public static class ItemCreateResponse{
        @NotEmpty
        @Schema(description = "물품명", example = "방석")
        private String name;

        @NotEmpty
        @Schema(description = "물품 총수량", example = "14")
        private int count;

        @NotEmpty
        @Schema(description = "물품 사진", example = "")
        private String image;
    }

    @Data
    @AllArgsConstructor
    public static class ItemCreateRequest{
        @NotEmpty
        @Schema(description = "물품명", example = "방석")
        private String name;

        @Schema(description = "물품 총수량", example = "13")
        private int count;

        @NotEmpty
        @Schema(description = "물품 사진", example = "")
        private MultipartFile image;
    }

    @Data
    @AllArgsConstructor
    public static class ItemUpdateRequest{
        @Schema(description = "물품 번호(id)", example = "1")
        private Long itemId;

        @Schema(description = "물품명", example = "방석")
        private String name;

        @Schema(description = "물품 총수량", example = "13")
        private Integer count;

        @Nullable
        @Schema(description = "물품 사진(생략가능)", example = "")
        private MultipartFile image;
    }

    @Data
    @AllArgsConstructor
    public static class ItemAllRequestExceptImage {
        @Schema(description = "물품번호", example = "1")
        private Long id;

        @Schema(description = "물품명", example = "방석")
        private String name;

        @Schema(description = "물품 총개수", example = "14")
        private int count;
    }

}

