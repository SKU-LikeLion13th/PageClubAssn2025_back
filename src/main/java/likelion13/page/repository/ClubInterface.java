package likelion13.page.repository;

import likelion13.page.domain.Club;
import likelion13.page.domain.Member;
import likelion13.page.domain.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClubInterface extends JpaRepository<Club, Long> {
    Long findIdByName (String clubName);
}
