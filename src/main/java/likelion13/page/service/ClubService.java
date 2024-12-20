package likelion13.page.service;

import likelion13.page.domain.Club;
import likelion13.page.domain.Event;
import likelion13.page.repository.ClubRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ClubService {
    private final ClubRepository clubRepository;

    // 동아리 추가
    @Transactional
    public Club addNewClub(String clubName, String description, MultipartFile logo) throws IOException {
        byte[] imageBytes = logo.getBytes();
        Club club = new Club(clubName, description, imageBytes);
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

    // 동아리 삭제
    @Transactional
    public boolean deleteClub(String clubName) {
        Club club = clubRepository.findByName(clubName);
        return clubRepository.deleteClub(club);
    }

    @Transactional
    public Club changeClub(Long id, String clubName, String description, MultipartFile logo) throws IOException {
        Club club = findById(id);
        System.out.println("logo = " + logo);

        if (logo != null) {
            club.setLogo(logo);
        }
        String newClubName = (clubName != null ? clubName : club.getName());
        String newDescription = (description != null ? description : club.getDescription());
        club.changeClub(newClubName, newDescription);

        return club;
    }
}