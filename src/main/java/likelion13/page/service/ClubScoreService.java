package likelion13.page.service;

import likelion13.page.DTO.ClubScoreDTO.*;
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
    public void saveOrUpdateScores(List<ClubScoreRequestDTO> requestDTOs) {
        for (ClubScoreRequestDTO dto : requestDTOs) {
            Club club = validateAndFindClub(dto.getClubName());
            Optional<ClubScore> existingScoreOptional = findExistingScore(dto.getQuarter(), dto.getRanking());

            if (existingScoreOptional.isPresent()) {
                updateExistingScore(existingScoreOptional.get(), club, dto.getScore());
            } else {
                saveNewScore(dto, club);
            }
        }
    }

    private Club validateAndFindClub(String clubName) {
        try {
            return clubRepository.findByName(clubName);
        } catch (Exception e) {
            throw new NotExistClubException("동아리 이름을 확인해주세요.", HttpStatus.BAD_REQUEST);
        }
    }

    private Optional<ClubScore> findExistingScore(String quarter, int ranking) {
        return clubScoreRepository.findByQuarterAndRanking(quarter, ranking);
    }

    private void updateExistingScore(ClubScore existingScore, Club club, int newScore) {
        existingScore.setScore(newScore);
        existingScore.setClub(club);
    }

    private void saveNewScore(ClubScoreRequestDTO dto, Club club) {
        ClubScore clubScore = new ClubScore();
        clubScore.setQuarter(dto.getQuarter());
        clubScore.setRanking(dto.getRanking());
        clubScore.setScore(dto.getScore());
        clubScore.setClub(club);

        clubScoreRepository.save(clubScore);
    }


    // 특정 분기의 점수 조회
    public List<ClubScoreResponseDTO> getTop3ScoresByQuarter(quarterRequestDTO requestDTO) {
        return clubScoreRepository.findByQuarter(requestDTO.getQuarter())
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

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
