package likelion13.page.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;



public class MemberClubDTO {

    // 모든 멤버 조회할 때 사용. 가입된 동아리를 리스트로 갖는 DTO
    @Data
    public static class MemberJoinedClubDTO {
        @Schema(description = "학번", example = "00000000")
        private Long memberId;

        @Schema(description = "학생 이름", example = "홍길동")
        private String name;

        @Schema(description = "학번", example = "00000000")
        private String studentId;

        @Schema(description = "가입 동아리 리스트", example = "[\"멋쟁이사자처럼\",\"애드마인\"]")
        private List<String> clubs;

        public MemberJoinedClubDTO(Long memberId, String name, String studentId, List<String> clubs) {
            this.memberId = memberId;
            this.name = name;
            this.studentId = studentId;
            this.clubs = clubs;
        }
    }

    // 단일 멤버 조회용. 멤버의 가입된 동아리, 미가입 동아리 전부 나타내는 DTO. joinedClub과 unJoinedClub을 Map으로 주지 않아도 괜찮나?
    // 단순히 3번, 4번 인덱스로 찾을 수 있나?
    @Data
    public static class MemberJoinedUnjoinedClubDTO {
        @Schema(description = "학번", example = "00000000")
        private Long memberId;

        @Schema(description = "학생 이름", example = "홍길동")
        private String name;

        @Schema(description = "학번", example = "00000000")
        private String studentId;

        @Schema(description = "가입 동아리 리스트", example = "[\"멋쟁이사자처럼\",\"애드마인\"]")
        private List<String> joinedClub;

        @Schema(description = "미가입 동아리 리스트", example = "[\"앱들\",\"신천지\",\"CCC\"]")
        private List<String> unjoinedClub;

        public MemberJoinedUnjoinedClubDTO(Long memberId, String name, String studentId, List<String> joinedClub, List<String> unjoinedClub) {
            this.memberId = memberId;
            this.name = name;
            this.studentId = studentId;
            this.joinedClub = joinedClub;
            this.unjoinedClub = unjoinedClub;
        }
    }
}
