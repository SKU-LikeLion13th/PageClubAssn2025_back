package likelion13.page.repository;

import likelion13.page.domain.ClubScore;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ClubScoreRepository extends JpaRepository<ClubScore, Long> {
    // 특정 분기의 데이터를 가져옴
    List<ClubScore> findByQuarter(String quarter);

    Optional<ClubScore> findByQuarterAndRanking(String quarter, int ranking);
}
