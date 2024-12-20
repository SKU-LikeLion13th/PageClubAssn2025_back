package likelion13.page.service;

import likelion13.page.DTO.EventDTO.AllEvents;
import likelion13.page.DTO.EventDTO.ResponsePuzzleForNotPart;
import likelion13.page.DTO.JoinEventDTO.ResponseJoinEvent;
import likelion13.page.domain.Event;
import likelion13.page.domain.JoinEvent;
import likelion13.page.domain.Member;
import likelion13.page.exception.ExistJoinEventException;
import likelion13.page.exception.NotExistJoinEventException;
import likelion13.page.repository.JoinEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class JoinEventService {
    private final JoinEventRepository joinEventRepository;
    private final EventService eventService;
    private final MemberService memberService;


    // 내가 참여한 이벤트 하나 이벤트 id와 studentId 로 조회
    public JoinEvent findJoinEvent(String studentId, Long eventId) {
        Member member = memberService.findByStudentId(studentId);
        Event event = eventService.findByEventId(eventId);
        JoinEvent joinEvent = joinEventRepository.findJoinEvent(member, event);
        if (joinEvent != null) {
            return joinEvent;
        } else {
            return null;
        }
    }

    // (관리자용) 멤버에게 참여한 이벤트(퍼즐) 추가 + 이미 있는 이벤트인지 확인 필요
    // 관리자 인증 로직 추가해야함
    @Transactional
    public JoinEvent saveJoinEvent(String studentId, Long eventId) {
        try{
            findJoinEvent(studentId,eventId);
        }catch(NotExistJoinEventException e){
            Member member = memberService.findByStudentId(studentId);
            Event event = eventService.findByEventId(eventId);

            JoinEvent joinEvent = new JoinEvent(member, event);

            return joinEventRepository.addJoinEvent(joinEvent);
        }
        throw new ExistJoinEventException("이미 이벤트를 참여한 적이 있습니다.", HttpStatus.NOT_ACCEPTABLE);
    }

    // (관리자용) 멤버의 이벤트(퍼즐) 삭제 => 잘못 넣었을 경우
    // 관리자 인증 로직 추가해야함
    @Transactional
    public boolean removeJoinEvent(String studentId, Long eventId) {
        JoinEvent joinEvent = findJoinEvent(studentId, eventId);
        joinEventRepository.removeJoinEvent(joinEvent);
        return true;
    }

    // 참여한 행사
    public List<ResponseJoinEvent> findAllJoinEvents(String studentId) {
        Member member = memberService.findByStudentId(studentId);
        List<ResponseJoinEvent> eventList = joinEventRepository.findAllPartEventsExceptImage(member);

        if (eventList.isEmpty()) {
            return null;
        } else {
            return eventList;
        }
    }

    // 참여하지 않은 행사(추가할 때)
    public List<ResponsePuzzleForNotPart> findNotPartEventsExceptImage(String studentId) {
        Member member = memberService.findByStudentId(studentId);
        List<ResponsePuzzleForNotPart> eventList = joinEventRepository.findNotPartEventsExceptImage(member);

        if (eventList.isEmpty()) {
            return null;
        } else {
            return eventList;
        }
    }

    public List<AllEvents> findEventsInfo(String studentId){
        return joinEventRepository.findEventsInfo(studentId);
    }
}