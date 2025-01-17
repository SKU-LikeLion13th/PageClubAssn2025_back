package likelion13.page.repository;

import likelion13.page.domain.Member;
import likelion13.page.domain.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberInterface extends JpaRepository<Member, Long> {
    void deleteByRole(RoleType roleType);
    List<Member> findByStudentIdIn(List<String> studentIds);
}
