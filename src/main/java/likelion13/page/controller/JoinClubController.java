package likelion13.page.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import likelion13.page.DTO.MemberDTO.*;
import likelion13.page.domain.Club;
import likelion13.page.security.JwtUtility;
import likelion13.page.service.JoinClubService;
import likelion13.page.service.MemberService;
import likelion13.page.service.MyPageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "멤버 페이지: 가입된 동아리 관련")
public class JoinClubController {
    private final JoinClubService joinClubService;
    private final MyPageService myPageService;
    private final JwtUtility jwtUtility;

    // 아이콘을 포함해서 가입되어있는 모든 조인클럽 반환.
    @GetMapping("/joined-list")
    @Operation(summary = "(택원) 멤버가 가입되어있는 모든 동아리 조회", description = "발급된 jwt 필요",
            responses = {@ApiResponse(responseCode="200", description="정상 로그인"),
                    @ApiResponse(responseCode = "403", description = "권한 없음")
            })
    public List<Club> findJoinedClubByMemberId(HttpServletRequest header){
        String studentId = jwtUtility.getStudentId(jwtUtility.resolveToken(header));
        return joinClubService.findJoinedClubByMemberId(studentId);
    }

    // iconclub 변경
    @PostMapping("/changeIconClub")
    @Operation(summary = "(택원) 대표 동아리 변경", description = "header:jwt, body:동아리명 필요",
            responses = {@ApiResponse(responseCode="200", description="변경 성공"),
                    @ApiResponse(responseCode = "403", description = "권한 없음")
            })
    public void changeIconClub(HttpServletRequest header, @RequestBody changeIconClub request){
        String studentId = jwtUtility.getStudentId(jwtUtility.resolveToken(header));
        System.out.println("request = " + request);
        myPageService.updateIconClub(studentId, request.getClubName());
    }
}
