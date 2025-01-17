package likelion13.page.service;

import likelion13.page.DTO.MemberClubDTO.MemberJoinedClubDTO;
import likelion13.page.DTO.MemberClubDTO.MemberJoinedUnjoinedClubDTO;
import likelion13.page.domain.Club;
import likelion13.page.domain.JoinClub;
import likelion13.page.domain.Member;
import likelion13.page.exception.ExistJoinClubException;
import likelion13.page.exception.NotExistJoinClubException;
import likelion13.page.repository.JoinClubRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static likelion13.page.DTO.JoinClubDTO.CreateJC;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class JoinClubService {
    private final JoinClubRepository joinClubRepository;
    private final MemberService memberService;
    private final ClubService clubService;

    // 기존 학생, 기존 동아리
    @Transactional
    public JoinClub saveNewMember(String studentId, String clubName) {
        Member member = memberService.findByStudentId(studentId);
        Club club = clubService.findByName(clubName);
        if(findJoinClub(studentId, club.getId())) {
            throw new ExistJoinClubException("이미 추가된 동아리원입니다.",HttpStatus.NOT_ACCEPTABLE);
        }
        JoinClub joinClub = new JoinClub(club, member);
        return joinClubRepository.saveNewMemberForClub(joinClub);
    }

    // 동아리에 가입된 학생들 찾기
    public List<JoinClub> findAllByClubName(String clubName) {
        Club club = clubService.findByName(clubName);
        return joinClubRepository.findAllByClubName(club);
    }

    // 학번으로 어느 동아리 가입되어있는지 조회
//    public List<JoinClub> findByStudentId(String studentId) {
//        return joinClubRepository.findByMemberId(studentId);
//    }

    // 학번으로 어느 동아리 가입되어있는지 조회
    public List<Club> findJoinedClubByMemberId(String studentId){
        return joinClubRepository.findJoinedClubByMemberId(studentId);
    }

    // 동아리에서 학생 탈퇴
    @Transactional
    public void deleteJoinClub(String studentId, Long clubId) {
        Member member = memberService.findByStudentId(studentId);
        Club club = clubService.findById(clubId);

        if (member.getIconClub()!=null && member.getIconClub().equals(club)) {
            member.setIconClub(null);
        }

        JoinClub joinClub = joinClubRepository.findJoinClub(club, member);

        joinClubRepository.deleteJoinClub(joinClub);
    }

    public boolean findJoinClub(String studentId, Long clubId){
        Member member = memberService.findByStudentId(studentId);

        Club club = clubService.findById(clubId);
        try{
            joinClubRepository.findJoinClub(club, member);
            return true;
        } catch (NotExistJoinClubException e) {
            return false;
        }
    }

    // 동아리원 검색
    public List<CreateJC> searchByKeyword(String keyword) {
        return joinClubRepository.findCMManageByKeyword(keyword);
    }

    public List<Club> findByStudentIdClub(String studentId){
        return joinClubRepository.findByStudentIdClub(studentId);
    }

    // 모든 멤버 가입된 동아리 반환
    public List<MemberJoinedClubDTO> findJoinedClubsForAllMember(){
        return joinClubRepository.findJoinedClubsForAllMember();
    }

    // 특정 멤버의 가입동아리, 미가입 동아리 반환
    public MemberJoinedUnjoinedClubDTO findJoinedClubUnJoinedClub(String studentId){
        return joinClubRepository.findJoinedClubUnJoinedClub(studentId);
    }


}
