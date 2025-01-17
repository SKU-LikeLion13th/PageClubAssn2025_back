package likelion13.page.service;

import jakarta.servlet.http.HttpServletRequest;
import likelion13.page.DTO.MemberDTO;
import likelion13.page.DTO.MemberDTO.MemberInfo;
import likelion13.page.domain.Member;
import likelion13.page.domain.RoleType;
import likelion13.page.exception.MemberExistException;
import likelion13.page.exception.MemberLoginException;
import likelion13.page.repository.MemberInterface;
import likelion13.page.repository.MemberRepository;
import likelion13.page.security.JwtUtility;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {
    private final ClubService clubService;
    private final MemberRepository memberRepository;
    private final JwtUtility jwtUtility;
    private final MemberInterface memberInterface;

    // 토큰으로 멤버 추출
    public Member tokenToMember(HttpServletRequest request) {
        Member member = findByStudentId(jwtUtility.getStudentId(jwtUtility.resolveToken(request)));
        if (member != null) {
            return member;
        } else {
            throw new MemberLoginException("동아리원만 이용 가능합니다.\n학번과 이름을 확인해주세요.", HttpStatus.BAD_REQUEST);
        }
    }

    // 동의 체크
    @Transactional
    public void updateAgree(MemberInfo request) {
        Member member = memberRepository.findByStudentId(request.getStudentId());

        if (member != null) {
            member.updateIsAgree();
        } else {
            throw new MemberLoginException("동아리원만 이용 가능합니다.\n학번과 이름을 확인해주세요.", HttpStatus.BAD_REQUEST);
        }

    }

    // 새로운 학생 추가
    @Transactional
    public Member addNewMember(String studentId, String name, RoleType role) {
//        Club iconClub = clubService.findByName(clubName);
        Member alreadyExistsMember = memberRepository.findByStudentId(studentId);
        if (alreadyExistsMember == null) {
            Member member = new Member(studentId, name, role);
            memberRepository.addNewMember(member);

            return member;
        }

        throw new MemberExistException("이미 존재하는 학번입니다.", HttpStatus.BAD_REQUEST);
    }

    // 테스트용
//    @Transactional
//    public Member addNewMember(String studentId, String name) {
//        if (isDuplicated(studentId)){
//            throw new DuplicatedStudentIdException(studentId + "번은 이미 가입된 학번입니다.");
//        }
//        Member member = new Member(studentId, name);
//        memberRepository.addNewMember(member);
//
//        return member;
//    }

    public boolean isDuplicated(String studentId){
        Member member = memberRepository.findByStudentId(studentId);
        return member == null;
    }


    // 학번으로 조회
    public Member findByStudentId(String studentId) {
        Member member = memberRepository.findByStudentId(studentId);
        if (member != null) {
            return member;
        } else {
            throw new MemberLoginException("학번이 올바른지 확인해주세요.", HttpStatus.BAD_REQUEST);
        }
    }

    public Member findByStudentIdWithoutException(String studentId) {
        return memberRepository.findByStudentId(studentId);
    }

    // 이름으로 조회
    public List<Member> findByName(String name) {
        return memberRepository.findByName(name);
    }

    // 전체 조회
    public List<Member> findAllMember() {
        return memberRepository.findAll();
    }

    // 학생 삭제
    @Transactional
    public void deleteMember(String studentId) {
        Member member = findByStudentId(studentId);
        memberRepository.deleteMember(member);
    }

    public List<MemberDTO.MemberInfo> findByKeyword(String keyword){
        return memberRepository.findByKeyword(keyword);
    }

    @Transactional
    public void deleteAllByRole() {
        memberInterface.deleteByRole(RoleType.ROLE_MEMBER);
    }
}