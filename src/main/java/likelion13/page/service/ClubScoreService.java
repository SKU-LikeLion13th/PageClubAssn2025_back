package likelion13.page.service;

import likelion13.page.DTO.ClubScoreDTO.*;
import likelion13.page.exception.ClubScoreNotFoundException;
import likelion13.page.exception.NotExistClubException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import likelion13.page.domain.Club;
import likelion13.page.domain.ClubScore;
import likelion13.page.repository.ClubRepository;
import likelion13.page.repository.ClubScoreRepository;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ClubScoreService {

    private final ClubRepository clubRepository;
    private final ClubScoreRepository clubScoreRepository;

    @Transactional
    public void saveOrUpdateScores(List<ClubScoreRequestDTO> requestDTO) {
        for (ClubScoreRequestDTO dto : requestDTO) {
            Club club = validateAndFindClub(dto.getClubName());

            Optional<ClubScore> existingScoreOptional = clubScoreRepository.findByClub_Name(dto.getClubName());

            if (existingScoreOptional.isPresent()) {
                updateExistingScore(existingScoreOptional.get(), dto.getScore(), dto.getQuarter());
            } else {
                saveNewScore(dto, club);
            }
        }
    }

    // 특정 클럽의 기존 점수 찾기
    private Club validateAndFindClub(String clubName) {
        return Optional.ofNullable(clubRepository.findByName(clubName))
                .orElseThrow(() -> new NotExistClubException("동아리 이름을 확인해주세요.", HttpStatus.BAD_REQUEST));
    }

    // 기존 점수 업데이트
    private void updateExistingScore(ClubScore existingScore, int score, String quarter) {
        existingScore.setScore(score);
        existingScore.setQuarter(quarter);

    }

    // 새로운 점수 저장
    private void saveNewScore(ClubScoreRequestDTO dto, Club club) {
        ClubScore clubScore = new ClubScore();
        clubScore.setScore(dto.getScore());
        clubScore.setClub(club);
        clubScore.setQuarter(dto.getQuarter());

        clubScoreRepository.save(clubScore);
    }

    // 순위 맵핑해서 넘겨주기
    public List<ClubScoreResponseDTO> getRankedScores() {
        List<ClubScore> scores = clubScoreRepository.findAllByOrderByScoreDesc();
        if (scores.isEmpty()) {
            throw new ClubScoreNotFoundException("점수 데이터가 존재하지 않습니다.");
        }

        // distinct한 점수 값별로 순위를 매기는 Map 생성
        Map<Integer, Integer> scoreToRank = new HashMap<>();
        int rank = 1;
        for (ClubScore cs : scores) {
            int scoreValue = cs.getScore();
            if (!scoreToRank.containsKey(scoreValue)) {
                scoreToRank.put(scoreValue, rank);
                rank++; // 다음 순위 증가
            }
        }


        // 동아리 점수를 순회하며 매핑된 순위를 할당하여 DTO 리스트 생성
        List<ClubScoreResponseDTO> rankedScores = new ArrayList<>();
        int lastRankIncluded = 0; // 현재 포함된 마지막 순위

        for (ClubScore cs : scores) {
            int clubRank = scoreToRank.get(cs.getScore());

            if (clubRank > 3) break;

            rankedScores.add(new ClubScoreResponseDTO(
                    cs.getQuarter(),
                    clubRank,
                    cs.getScore(),
                    cs.getClub().getName(),
                    cs.getClub().getLogo()
            ));

            lastRankIncluded = clubRank;
        }

        return rankedScores;
    }

    //점수데이터 내림차순으로 넘겨주기(관리자페이지에서는 이거 쓰는게 나을듯?)
    public List<ClubScoreAdminResponseDTO> getAllScores() {
        List<ClubScore> scores = clubScoreRepository.findAllByOrderByScoreDesc();
        if (scores.isEmpty()) {
            throw new ClubScoreNotFoundException("점수 데이터가 존재하지 않습니다.");
        }

        return scores.stream()
                .map(cs -> new ClubScoreAdminResponseDTO(
                        cs.getQuarter(),
                        cs.getScore(),
                        cs.getClub().getName(),
                        cs.getId()
                ))
                .collect(Collectors.toList());
    }

    // club_id로 찾아서 삭제하기
    @Transactional
    public void deleteClubScore(Long clubId) {
        clubScoreRepository.findByClub_Id(clubId)
                .ifPresent(clubScoreRepository::delete);
    }
}
