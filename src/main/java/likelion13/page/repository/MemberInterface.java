package likelion13.page.repository;

import likelion13.page.domain.Member;
import likelion13.page.domain.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberInterface extends JpaRepository<Member, Long> {
    void deleteAllByRole(RoleType roleType);
}
