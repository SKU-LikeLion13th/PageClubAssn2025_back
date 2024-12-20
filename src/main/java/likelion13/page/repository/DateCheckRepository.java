package likelion13.page.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import likelion13.page.domain.DateCheck;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
@RequiredArgsConstructor
public class DateCheckRepository {
    private final EntityManager em;
    public DateCheck save(DateCheck date){
        em.persist(date);
        return date;
    }

    public DateCheck findById(Long id){
        return em.find(DateCheck.class, id);
    }

    public DateCheck findByDate(LocalDate date){
        try{
            return em.createQuery("select dc from DateCheck dc where dc.date = :date", DateCheck.class)
                    .setParameter("date", date).getSingleResult();
        }catch (NoResultException e) {
            return null;
        }
    }

//    public DateCheck findBookDay(LocalDate date){
//        try{
//            return em.createQuery("select d from DateCheck d where d.nextBizDay = :today order by d.date asc", DateCheck.class)
//                    .setParameter("today",date).getSingleResult();
//        }catch (NoResultException e){
//            return null;
//        }
//    }

    public void remove(DateCheck date){
        em.remove(date);
    }
}
