package likelion13.page.repository;

import likelion13.page.domain.ClubScore;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ClubScoreRepository extends JpaRepository<ClubScore, Long> {

    Optional<ClubScore> findByRanking(int ranking); // 순위 기준 데이터 조회
    List<ClubScore> findAllByOrderByRanking(); // 전체 데이터 순위 기준 정렬 후 반환

}
