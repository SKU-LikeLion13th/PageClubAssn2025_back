package likelion13.page.repository;

import likelion13.page.domain.Member;
import likelion13.page.domain.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface MemberInterface extends JpaRepository<Member, Long> {
    @Modifying
    @Transactional
    @Query("DELETE FROM Member m WHERE m.role = :role")
    void deleteByRole(@Param("role") RoleType role);
    List<Member> findByStudentIdIn(List<String> studentIds);
}
