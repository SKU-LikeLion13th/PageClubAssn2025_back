package likelion13.page.repository;

import jakarta.persistence.EntityManager;
import likelion13.page.DTO.MemberClubDTO.MemberJoinedClubDTO;
import likelion13.page.DTO.MemberClubDTO.MemberJoinedUnjoinedClubDTO;
import likelion13.page.domain.Club;
import likelion13.page.domain.JoinClub;
import likelion13.page.domain.Member;
import likelion13.page.exception.MemberLoginException;
import likelion13.page.exception.NotExistJoinClubException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;

import java.util.List;

import static likelion13.page.DTO.JoinClubDTO.CreateJC;

@Repository
@RequiredArgsConstructor
public class JoinClubRepository {
    private final EntityManager em;

    // 동아리원 추가
    public JoinClub saveNewMemberForClub(JoinClub joinClub) {
        em.persist(joinClub);
        return joinClub;
    }

    // 학번으로 가입된 동아리 찾기
    public List<JoinClub> findByMemberId(String studentId) {
        return em.createQuery("select jc.club from JoinClub jc where jc.member.studentId =:id", JoinClub.class)
                .setParameter("id", studentId).getResultList();
    }

    // 학번으로 가입된 동아리 찾기
    public List<Club> findJoinedClubByMemberId(String studentId){
        return em.createQuery("SELECT jc.club FROM JoinClub jc WHERE jc.member.studentId =:studentId", Club.class)
                .setParameter("studentId", studentId).getResultList();
    }

    // iconClub이 null이면 해당 멤버의 club중 하나 세팅해야함 (위에 함수랑 다르게 club을 반환함.)
    public List<Club> findByStudentIdClub(String studentId) {
        return em.createQuery("SELECT jc.club FROM JoinClub jc WHERE jc.member.studentId =:studentId", Club.class)
                .setParameter("studentId", studentId)
                .getResultList();
    }

    // 동아리에 있는 동아리원 조회
    public List<JoinClub> findAllByClubName(Club club) {
        return em.createQuery("select jc.member from JoinClub jc where jc.club = :club", JoinClub.class)
                .setParameter("club", club).getResultList();
    }

    // 동아리 이름과 학번으로 찾기
    public JoinClub findJoinClub(Club club, Member student) {
        try {
            return em.createQuery("select jc from JoinClub jc where jc.member = :student and jc.club = :club", JoinClub.class)
                    .setParameter("student", student).setParameter("club", club)
                    .getSingleResult();
        } catch (Exception e) {
            throw new NotExistJoinClubException("일치하는 결과가 없습니다.", HttpStatus.BAD_REQUEST);
        }
    }

    // 동아리에서 해당 학생 삭제 (동아리 탈퇴)
    public void deleteJoinClub(JoinClub joinClub) {
        em.remove(joinClub);
    }

    // 동아리원 검색
    public List<CreateJC> findCMManageByKeyword(String keyword) {
        return em.createQuery("SELECT new CreateJC(m.studentId, m.name, c.name) " +
                        "FROM JoinClub jc " +
                        "INNER JOIN jc.member m " +
                        "INNER JOIN jc.club c " +
                        "WHERE m.name LIKE :keyword OR m.studentId LIKE :keyword OR c.name LIKE :keyword", CreateJC.class)
                .setParameter("keyword", "%" + keyword + "%")
                .getResultList();
    }

    // 모든 학생의 가입된 동아리 정보
    public List<MemberJoinedClubDTO> findJoinedClubsForAllMember(){
        List<Object[]> members = em.createQuery("SELECT m.id, m.name, m.studentId FROM Member m", Object[].class)
                .getResultList();

        List<MemberJoinedClubDTO> memberClubDTOs = members.stream().map(member -> {
            Long memberId = (Long) member[0];
            String name = (String) member[1];
            String studentId = (String) member[2];

            List<String> clubs = em.createQuery(
                            "SELECT c.name FROM JoinClub jc JOIN jc.club c WHERE jc.member.id = :memberId", String.class)
                    .setParameter("memberId", memberId)
                    .getResultList();
            return new MemberJoinedClubDTO(memberId, name, studentId, clubs);
        }).toList();

        return memberClubDTOs;
    }

    // 학번으로 조회. 학생의 가입 클럽, 미가입 클럽 정보 반환
    public MemberJoinedUnjoinedClubDTO findJoinedClubUnJoinedClub(String studentId) {
        try {
            Member member = em.createQuery("SELECT m FROM Member m WHERE m.studentId =:studentId", Member.class)
                    .setParameter("studentId", studentId)
                    .getSingleResult();

            // 학생이 가입한 클럽 조회
            List<String> joinedClubs = em.createQuery(
                            "SELECT jc.club.name FROM JoinClub jc  WHERE jc.member.studentId = :studentId", String.class)
                    .setParameter("studentId", studentId)
                    .getResultList();

            List<String> allClubs = em.createQuery("SELECT c.name FROM Club c", String.class).getResultList();
            List<String> unjoinedClubs = allClubs.stream().filter(club -> !joinedClubs.contains(club)).toList();

            return new MemberJoinedUnjoinedClubDTO(
                    member.getId(),
                    member.getName(),
                    studentId,
                    joinedClubs,
                    unjoinedClubs);
        } catch (Exception e) {
            throw new MemberLoginException("학번을 확인해주세요.", HttpStatus.BAD_REQUEST);
        }
    }
}