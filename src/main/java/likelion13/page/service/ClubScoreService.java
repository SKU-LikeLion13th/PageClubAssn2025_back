package likelion13.page.service;

import likelion13.page.DTO.ClubScoreDTO.*;
import likelion13.page.exception.ClubScoreNotFoundException;
import likelion13.page.exception.DuplicatedRankingException;
import likelion13.page.exception.NotExistClubException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import likelion13.page.domain.Club;
import likelion13.page.domain.ClubScore;
import likelion13.page.DTO.ClubScoreDTO.ClubScoreRequestDTO;
import likelion13.page.DTO.ClubScoreDTO.ClubScoreResponseDTO;
import likelion13.page.repository.ClubRepository;
import likelion13.page.repository.ClubScoreRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ClubScoreService {

    private final ClubRepository clubRepository;
    private final ClubScoreRepository clubScoreRepository;

    @Transactional

    //점수를 저장하거나 업데이트하는 메서드
    public void saveOrUpdateScores(List<ClubScoreRequestDTO> requestDTO) {

        validateUniqueRanking(requestDTO);

        for (ClubScoreRequestDTO dto : requestDTO) {
            Club club = validateAndFindClub(dto.getClubName());

            Optional<ClubScore> existingScoreOptional = findExistingScore(dto.getRanking());

            if (existingScoreOptional.isPresent()) {

                updateExistingScore(existingScoreOptional.get(), club, dto);

            } else {

                saveNewScore(dto, club);

            }
        }
    }

    //동아리 이름으로 동아리를 검증하고 조회하는 메서드
    private Club validateAndFindClub(String clubName) {
        try {
            return clubRepository.findByName(clubName);
        } catch (Exception e) {
            throw new NotExistClubException("동아리 이름을 확인해주세요.", HttpStatus.BAD_REQUEST);
        }
    }

    //순위를 기준으로 기존 점수를 조회하는 메서드
    private Optional<ClubScore> findExistingScore(int ranking) {
        return clubScoreRepository.findByRanking(ranking);
    }

    //기존 점수를 업데이트하는 메서드
    private void updateExistingScore(ClubScore existingScore, Club club, ClubScoreRequestDTO dto) {
        existingScore.setScore(dto.getScore());
        existingScore.setClub(club);
        existingScore.setQuarter(dto.getQuarter());
    }

    //순위 중복 검사
    private void validateUniqueRanking(List<ClubScoreRequestDTO> requestDTOs) {
        long duplicateCount = requestDTOs.stream()
                .map(ClubScoreRequestDTO::getRanking)
                .distinct()
                .count();
        if (duplicateCount != requestDTOs.size()) {
            throw new DuplicatedRankingException("요청 데이터에 중복된 순위가 있습니다.");
        }
    }


    //새로운 점수를 저장하는 메서드
    private void saveNewScore(ClubScoreRequestDTO dto, Club club) {
        ClubScore clubScore = new ClubScore();
        clubScore.setQuarter(dto.getQuarter());
        clubScore.setRanking(dto.getRanking());
        clubScore.setScore(dto.getScore());
        clubScore.setClub(club);

        clubScoreRepository.save(clubScore);
    }


    // 순위를 기준으로 정렬된 점수를 반환
    public List<ClubScoreResponseDTO> getAllScores() {
        List<ClubScore> scores = clubScoreRepository.findAllByOrderByRanking();
        if (scores.isEmpty()) {
            throw new ClubScoreNotFoundException("점수 데이터가 존재하지 않습니다.");
        }
        return scores.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }


    //ClubScore 엔티티를 ClubScoreResponseDTO로 변환하는 메서드
    private ClubScoreResponseDTO convertToResponseDTO(ClubScore clubScore) {
        return new ClubScoreResponseDTO(
                clubScore.getQuarter(),
                clubScore.getRanking(),
                clubScore.getScore(),
                clubScore.getClub().getName(),
                clubScore.getClub().getLogo()
        );
    }
}
