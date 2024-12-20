package likelion13.page.repository;

import jakarta.persistence.EntityManager;
import likelion13.page.DTO.ClubDTO;
import likelion13.page.DTO.ClubDTO.*;
import likelion13.page.domain.Club;
import likelion13.page.exception.MemberLoginException;
import likelion13.page.exception.NotExistClubException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ClubRepository {
    private final EntityManager em;

    // 새로운 동아리 추가
    public Club addNewClub(Club club) {
        em.persist(club);

        return club;
    }

    public Club findById(Long id){
        return em.find(Club.class, id);
    }

    // 동아리 조회
    public Club findByName(String clubName) {
        try {
            return em.createQuery("select c from Club c where c.name =:clubName", Club.class)
                    .setParameter("clubName", clubName).getSingleResult();
        } catch (Exception e) {
            throw new NotExistClubException("동아리 이름을 확인해주세요.", HttpStatus.BAD_REQUEST);
        }
    }

    public List<Club> findAll(){
        return em.createQuery("SELECT c FROM Club c", Club.class).getResultList();
    }

    // 동아리 삭제
    public boolean deleteClub(Club club) {
        em.remove(club);

        return true;
    }
}