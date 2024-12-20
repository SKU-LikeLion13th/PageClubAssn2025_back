package likelion13.page.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import likelion13.page.DTO.MemberDTO.MemberInfo;
import likelion13.page.DTO.MemberDTO.ResponseLogin;
import likelion13.page.DTO.MemberDTO.ResponseMain;
import likelion13.page.domain.Member;
import likelion13.page.security.JwtUtility;
import likelion13.page.service.MemberService;
import likelion13.page.service.MyPageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "멤버 페이지: 기본")
public class MemberController {
    private final MemberService memberService;
    private final MyPageService myPageService;
    private final JwtUtility jwtUtility;

    @Operation(summary = "(호주) 로그인", description = "request: 학번, 이름\nresponse: jwt",
            responses = {@ApiResponse(responseCode = "200", description = "로그인 성공"),
                    @ApiResponse(responseCode = "400", description = "학번 혹은 이름이 틀렸을 경우"),
                    @ApiResponse(responseCode = "401", description = "개인정보 동의하지 않았을 경우")})
    @PostMapping("/login")
    public ResponseEntity<ResponseLogin> login(@RequestBody MemberInfo request) {
        System.out.println("request = " + request.getName());
        return ResponseEntity.ok(myPageService.login(request));
    }

    @Operation(summary = "(호주) 개인정보 동의서 api", description = "request: 학번, 이름\nresponse: jwt",
            responses = {@ApiResponse(responseCode = "200", description = "로그인 성공")})
    @PostMapping("/agree")
    public ResponseEntity<ResponseLogin> checkAgree(@RequestBody MemberInfo request) {
        memberService.updateAgree(request);

        return ResponseEntity.ok(myPageService.login(request));
    }

    @Operation(summary = "(호주) 마이페이지", description = "request: 발급된 jwt 필드를 헤더에서 받아옴\nresponse: 학번, 이름, 동아리 이미지",
            responses = {@ApiResponse(responseCode = "200", description = "")})
    @GetMapping("/mypage")
    public ResponseEntity<ResponseMain> mainPage(HttpServletRequest header) {
        Member member = memberService.tokenToMember(header);

        return ResponseEntity.ok(new ResponseMain(member));
    }

    // 테스트용
//    @PostMapping("/add")
//    public ResponseMember addMember(@RequestBody RequestMember request) {
//        Member member = memberService.addNewMember(request.getStudentId(), request.getName());
//
//        return new ResponseMember(member.getStudentId(), member.getName(), "asdf");
//    }
}