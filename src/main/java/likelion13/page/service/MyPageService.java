package likelion13.page.service;

import likelion13.page.DTO.MemberDTO;
import likelion13.page.domain.Club;
import likelion13.page.domain.Member;
import likelion13.page.exception.MemberLoginException;
import likelion13.page.exception.NoJoinedClubException;
import likelion13.page.exception.NotExistJoinClubException;
import likelion13.page.security.JwtUtility;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MyPageService {

    private final MemberService memberService;
    private final ClubService clubService;
    private final JoinClubService joinClubService;
    private final JwtUtility jwtUtility;

    // 로그인을 위해서 member조회할 때 iconClub이 null이면 이 함수 사용할 것.
    private void setRandomIconClub(Member member){
        List<Club> joinedClubs = joinClubService.findByStudentIdClub(member.getStudentId());

        if (joinedClubs.isEmpty()) {
            throw new NoJoinedClubException("동아리 연합회 소속이 아닙니다.", HttpStatus.BAD_REQUEST);
        } else {
            member.updateIconClub(joinedClubs.get(0));
        }
    }

    @Transactional
    // 대표 동아리 변경
    public Member updateIconClub(String studentId, Long clubId) {
        Member member = memberService.findByStudentId(studentId);
        Club newIconClub = clubService.findById(clubId);
        if(!joinClubService.findJoinClub(studentId, clubId)){
            throw new NotExistJoinClubException("해당 동아리에 가입되어있지 않습니다.", HttpStatus.BAD_REQUEST);
        }
        member.updateIconClub(newIconClub);
        return member;
    }

    // 로그인
    @Transactional
    public MemberDTO.ResponseLogin login(MemberDTO.MemberInfo request) {
        Member member = memberService.findByStudentId(request.getStudentId());
        System.out.println("request = " + request);

        if (member == null || !member.getName().equals(request.getName())) {
            throw new MemberLoginException("동아리원만 이용 가능합니다.\n학번과 이름을 확인해주세요.", HttpStatus.BAD_REQUEST);
        }

        if(member.getIconClub()==null){
            setRandomIconClub(member);
        }

        if (!member.isAgree()) {
            throw new MemberLoginException("개인정보 동의가 필요합니다.", HttpStatus.UNAUTHORIZED);
        }

        return new MemberDTO.ResponseLogin(jwtUtility.generateToken(member.getStudentId()), member.getRole());
    }

}
