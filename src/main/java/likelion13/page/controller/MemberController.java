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
import org.springframework.http.HttpStatus;
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

    @Operation(summary = "로그인", description = "body에 json 형태로 학번, 이름 필요",
            responses = {@ApiResponse(responseCode = "200", description = "로그인 성공"),
                    @ApiResponse(responseCode = "400", description = "학번 혹은 이름이 틀렸을 경우"),
                    @ApiResponse(responseCode = "401", description = "개인정보 동의하지 않았을 경우")})
    @PostMapping("/login")
    public ResponseEntity<ResponseLogin> login(@RequestBody MemberInfo request) {
        System.out.println("request = " + request.getName());
        return ResponseEntity.status(HttpStatus.OK).body(myPageService.login(request));
    }

    @Operation(summary = "개인정보 동의서 api", description = "body에 json 형태로 학번, 이름 필요",
            responses = {@ApiResponse(responseCode = "200", description = "로그인 성공")})
    @PostMapping("/agree")
    public ResponseEntity<ResponseLogin> checkAgree(@RequestBody MemberInfo request) {
        memberService.updateAgree(request);

        return ResponseEntity.status(HttpStatus.OK).body(myPageService.login(request));
    }

    @Operation(summary = "마이페이지", description = "Header에 Bearer token 필요",
            responses = {@ApiResponse(responseCode = "200", description = "")})
    @GetMapping("/mypage")
    public ResponseEntity<ResponseMain> mainPage(HttpServletRequest header) {
        Member member = memberService.tokenToMember(header);

        return ResponseEntity.status(HttpStatus.OK).body(new ResponseMain(member));
    }

    // 테스트용
//    @PostMapping("/add")
//    public ResponseMember addMember(@RequestBody RequestMember request) {
//        Member member = memberService.addNewMember(request.getStudentId(), request.getName());
//
//        return new ResponseMember(member.getStudentId(), member.getName(), "asdf");
//    }
}