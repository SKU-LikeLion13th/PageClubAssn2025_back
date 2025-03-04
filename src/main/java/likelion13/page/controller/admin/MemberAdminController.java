package likelion13.page.controller.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import likelion13.page.DTO.MemberDTO;
import likelion13.page.DTO.MemberDTO.AddMemberResponse;
import likelion13.page.DTO.MemberDTO.AddRequestMember;
import likelion13.page.domain.Member;
import likelion13.page.exception.ExistJoinClubException;
import likelion13.page.exception.InvaildExcelFileException;
import likelion13.page.exception.MemberExistException;
import likelion13.page.exception.NotExistClubException;
import likelion13.page.service.ExcelReaderService;
import likelion13.page.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
@Tag(name = "관리자 페이지: 학생 관리 관련")
public class MemberAdminController {
    private final MemberService memberService;
    private final ExcelReaderService excelReaderService;

    @Operation(summary = "새로운 학생 추가", description = "body에 json 형태로 학번, 성함, 권한 필요",
            responses = {@ApiResponse(responseCode = "201", description = "생성 성공 후 joinClub 객체 반환"),
                        @ApiResponse(responseCode = "", description = "")})
    @PostMapping("/member/add")
    public ResponseEntity<AddMemberResponse> addMember(@RequestBody AddRequestMember request) {
        Member member = memberService.addNewMember(request.getStudentId(), request.getName(), request.getRole());

        return ResponseEntity.status(HttpStatus.CREATED).body(new AddMemberResponse(member.getStudentId(), member.getName(), member.getRole()));
    }

    @Operation(summary = "학생 삭제", description = "쿼리 파라미터로 학번 필요",
            responses = {@ApiResponse(responseCode = "200", description = "삭제 후 삭제 성공 메세지 반환"),
                    @ApiResponse(responseCode = "400", description = "올바르지 않은 학번 입력 시 \"학번이 올바른지 확인해주세요.\" 메세지 반환")})
    @DeleteMapping("/member/delete")
    public ResponseEntity<String> deleteMember(@RequestParam String studentId) {
        memberService.deleteMember(studentId);

        return ResponseEntity.status(HttpStatus.OK).body("삭제되었습니다.");
    }

    @Operation(summary = "등록된 학생 조회(동아리와 무관)", description = "쿼리 파라미터로 학번 또는 이름 필요\n(학번 또는 이름으로 학생 정보 조회)",
            responses = {@ApiResponse(responseCode = "200", description = "조회 성공")})
    @GetMapping("/member/find")
    public ResponseEntity<List<MemberDTO.MemberInfo>> findMemberWithKeyword(@RequestParam String keyword) {

        return ResponseEntity.status(HttpStatus.OK).body(memberService.findByKeyword(keyword));
    }

    @Operation(summary = "(민규) Excel(.xlsx) 파일로 동아리원 추가", description = "body에 form-data로 Excel 파일 필요",
            responses = {@ApiResponse(responseCode = "201", description = "등록 성공"),
                    @ApiResponse(responseCode = "400", description = "1. 멤버로 추가하는 과정에서 오류가 발생했습니다. 멋쟁이사자처럼 13기에 문의해 주세요!\n2. 엑셀 파일의 데이터를 읽을 수 없습니다. 다시 한번 확인해 주세요!\n3. 동아리 이름을 확인해 주세요!"),
                    @ApiResponse(responseCode = "409", description = "각 동아리에 중복된 학번이 없는지 확인해 주세요!")})
    @PostMapping("/excel/upload")
    public ResponseEntity<?> uploadExcel(MultipartFile file) {
        try {
            // Excel 파일을 읽어 데이터를 반환
            excelReaderService.readExcel(file);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (MemberExistException e) {
            throw new InvaildExcelFileException(400, "멤버로 추가하는 과정에서 오류가 발생했습니다. 멋쟁이사자처럼 13기에 문의해 주세요!");
        } catch (NotExistClubException e) {
            throw new InvaildExcelFileException(400, "동아리 이름을 확인해 주세요!");
        } catch (ExistJoinClubException e) {
            throw new InvaildExcelFileException(409, "각 동아리에 중복된 학번이 없는지 확인해 주세요!");
        }  catch (Exception e) {
            e.printStackTrace();
            throw new InvaildExcelFileException(400, "엑셀 파일의 데이터를 읽을 수 없습니다. 다시 한번 확인해 주세요!");
        }
    }

    @Operation(summary = "(민규) Role이 ROLE_MEMBER 전체 삭제", description = "",
            responses = {@ApiResponse(responseCode = "200", description = "삭제 성공")})
    @DeleteMapping("/all-member")
    public ResponseEntity<?> deleteAllByRole() {
        memberService.deleteAllByRole();
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}