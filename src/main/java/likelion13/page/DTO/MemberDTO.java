package likelion13.page.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import likelion13.page.domain.Member;
import likelion13.page.domain.RoleType;
import lombok.Data;

public class MemberDTO {
    @Data
    public static class ResponseMember {
        @Schema(description = "학번", example = "00000000")
        private String studentId;
        @Schema(description = "이름", example = "홍길동")
        private String name;
        @Schema(description = "동아리명", example = "멋쟁이사자처럼")
        private String clubName;

        public ResponseMember(String studentId, String name, String clubName) {
            this.studentId = studentId;
            this.name = name;
            this.clubName = clubName;
        }
    }

    // 로그인 할 때
    @Data
    public static class MemberInfo {
        @Schema(description = "학번", example = "20190826")
        private String studentId;

        @Schema(description = "이름", example = "홍길동")
        private String name;

        public MemberInfo(String studentId, String name) {
            this.studentId = studentId;
            this.name = name;
        }
    }

    @Data
    public static class RequestAgree {
        @Schema(description = "학번", example = "00000000")
        private String studentId;
        @Schema(description = "이름", example = "홍길동")
        private String name;
        @Schema(description = "동아리명", example = "멋쟁이사자처럼")
        private Boolean isAgree;
    }

    @Data
    public static class ResponseMain {
        @Schema(description = "이름", example = "홍길동")
        private String name;
        @Schema(description = "동아리명", example = "멋쟁이사자처럼")
        private String clubName;
        @Schema(description = "사진 반환형(문자열)", example = "대충 엄청 긴 문자열")
        private String logo;

        public ResponseMain(Member member) {
            this.name = member.getName();
            this.clubName = member.getIconClub().getName();
            this.logo = member.getIconClub().arrayToImage();
        }
    }

    @Data
    public static class ResponseLogin {
        @Schema(description = "JWT 토큰", example = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIwMDAwMDAwMCIsImlhdCI6MTcxMTgxMDc2NSwiZXhwIjoyMDcxODEwNzY1fQ.2gbH5s0ODmTE59NRrFi9Fd8kqahHsfQqgHu6NQjjte1_4abMHmI6VfSKVI46SjftueKXSDFVr8WATiuf1ZMNzg")
        private String accessToken;
        private RoleType role;

        public ResponseLogin(String accessToken, RoleType role) {
            this.accessToken = accessToken;
            this.role = role;
        }

//        public ResponseLogin(String accessToken) {
//            this.accessToken = accessToken;
//        }
    }

    @Data
    public static class AddRequestMember {
        @Schema(description = "학번", example = "00000000")
        private String studentId;
        @Schema(description = "이름", example = "홍길동")
        private String name;
        @Schema(description = "권한", example = "ROLE_ADMIN")
        private RoleType role;
    }

    @Data
    public static class AddMemberResponse {
        @Schema(description = "학번", example = "00000000")
        private String studentId;
        @Schema(description = "이름", example = "홍길동")
        private String name;
        @Schema(description = "권한", example = "ROLE_ADMIN")
        private RoleType role;

        public AddMemberResponse(String studentId, String name, RoleType role) {
            this.studentId = studentId;
            this.name = name;
            this.role = role;
        }
    }

    @Data
    public static class changeIconClub {
        @Schema(description = "바꿀 동아리명", example = "멋쟁이사자처럼")
        private String clubName;
    }

    @Data
    public static class RequestMemberId {
        @Schema(description = "조회학번", example = "00000000")
        private String studentId;
    }
}
