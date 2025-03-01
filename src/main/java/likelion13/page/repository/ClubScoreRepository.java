package likelion13.page.repository;

import likelion13.page.domain.ClubScore;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ClubScoreRepository extends JpaRepository<ClubScore, Long> {

    Optional<ClubScore> findByClub_Name(String clubName); // 특정 클럽의 점수 조회
    List<ClubScore> findAllByOrderByScoreDesc(); // 점수 기준 내림차순 정렬 후 반환
    Optional<ClubScore> findByClub_Id(Long clubId);
}
