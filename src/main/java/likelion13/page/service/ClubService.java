package likelion13.page.service;

import likelion13.page.domain.Club;
import likelion13.page.repository.ClubInterface;
import likelion13.page.repository.ClubRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ClubService {
    private final ClubRepository clubRepository;
    private final ClubInterface clubInterface;

    // 동아리 추가
    @Transactional
    public Club addNewClub(String clubName, MultipartFile logo) throws IOException {
        byte[] imageBytes = logo.getBytes();
        Club club = new Club(clubName, imageBytes);
        return clubRepository.addNewClub(club);
    }

    public Club findById(Long id){
        return clubRepository.findById(id);
    }

    public List<Club> findAll() {
        return clubRepository.findAll();
    }

    // 동아리 조회
    public Club findByName(String clubName) {
        return clubRepository.findByName(clubName);
    }

    public Long findIdByClubName(String clubName) {
        return clubInterface.findIdByName(clubName);
    }

    // 동아리 삭제
    @Transactional
    public boolean deleteClub(Long id) {
        Club club = clubRepository.findById(id);
        return clubRepository.deleteClub(club);
    }

    @Transactional
    public Club changeClub(Long id, String clubName, MultipartFile logo) throws IOException {
        Club club = findById(id);
        System.out.println("logo = " + logo);

        if (logo != null) {
            club.setLogo(logo);
        }
        String newClubName = (clubName != null ? clubName : club.getName());
        club.changeClub(newClubName);

        return club;
    }
}