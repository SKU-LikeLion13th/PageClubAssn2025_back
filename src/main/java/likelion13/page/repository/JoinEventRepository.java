package likelion13.page.repository;

import jakarta.persistence.EntityManager;
import likelion13.page.DTO.EventDTO.*;
import likelion13.page.DTO.JoinEventDTO.*;
import likelion13.page.domain.Event;
import likelion13.page.domain.JoinEvent;
import likelion13.page.domain.Member;
import likelion13.page.exception.NotExistJoinEventException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;


@Repository
@RequiredArgsConstructor
public class JoinEventRepository {

    private final EntityManager em;

    // 내가 참여한 이벤트 하나 이벤트 id와 studentId 로 조회
    public JoinEvent findJoinEvent(Member member, Event event) {
        try {
            return em.createQuery("SELECT je FROM JoinEvent je WHERE je.member = :member and je.event = :event", JoinEvent.class)
                    .setParameter("member", member).setParameter("event", event).getSingleResult();
        } catch (Exception e) {
            throw new NotExistJoinEventException("일치하는 결과가 없습니다.", HttpStatus.BAD_REQUEST);
        }
    }

    // (관리자용) 멤버에게 참여한 이벤트(퍼즐) 추가
    public JoinEvent addJoinEvent(JoinEvent joinEvent) {
        em.persist(joinEvent);
        return joinEvent;
    }

    // (관리자용) 멤버의 이벤트(퍼즐) 삭제 => 잘못 넣었을 경우
    public boolean removeJoinEvent(JoinEvent joinEvent) {
        em.remove(joinEvent);
        return true;
    }

    // 내가 참여한 행사 리스트 반환(이미지 빼고)
    public List<ResponseJoinEvent> findAllPartEventsExceptImage(Member member) {
        return em.createQuery("SELECT new ResponseJoinEvent(je.event.id, je.event.name, je.checkDate) FROM JoinEvent je WHERE je.member = :member", ResponseJoinEvent.class)
                .setParameter("member", member).getResultList();
    }

    // 내가 참여하지 않은 행사 리스트 반환(이미지 빼고)
    public List<ResponsePuzzleForNotPart> findNotPartEventsExceptImage(Member member) {
        return em.createQuery(
                        "SELECT new ResponsePuzzleForNotPart(e.id, e.name) " +
                                "FROM Event e " +
                                "WHERE e NOT IN (SELECT distinct je.event FROM JoinEvent je WHERE je.member = :member)",
                        ResponsePuzzleForNotPart.class)
                .setParameter("member", member).getResultList();
    }

    public List<AllEvents> findEventsInfo(String studentId){
        List<Event> eventList = em.createQuery("SELECT e FROM Event e", Event.class).getResultList();

        // 해당 학생이 참여한 이벤트의 ID 목록 조회
        List<Long> joinedEventIds = em.createQuery(
                        "SELECT je.event.id FROM JoinEvent je WHERE je.member.studentId = :studentId", Long.class)
                .setParameter("studentId", studentId)
                .getResultList();

        // 이벤트 리스트를 AllEvents DTO로 변환
        List<AllEvents> allEventsList = eventList.stream().map(event -> {
            AllEvents allEvents = new AllEvents(event.getId(), event.getName(), event.arrayToImage(), event.getDate(), joinedEventIds.contains(event.getId()));
            return allEvents;
        }).collect(Collectors.toList());

        return allEventsList;
    }
}
