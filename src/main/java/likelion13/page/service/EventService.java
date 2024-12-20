package likelion13.page.service;


import likelion13.page.domain.Event;
import likelion13.page.domain.Member;
import likelion13.page.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static likelion13.page.DTO.EventDTO.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EventService {

    private final EventRepository eventRepository;
    private final MemberService memberService;


    // 이벤트 추가
    @Transactional
    public Event addEvent(String name, MultipartFile image, LocalDate date) throws IOException {
        byte[] imageBytes = image.getBytes();
        Event event = new Event(name, imageBytes, date);
        return eventRepository.addEvent(event);
    }

    @Transactional
    public Event changeEvent(Long eventId, String name, LocalDate date, MultipartFile poster) throws IOException {

        Event event = findByEventId(eventId);
        // 사진 넣으면 바꾼 사진으로
        if (poster != null) {
            event.setPoster(poster);
        }
        event.changeEvent((name != null ? name : event.getName()), (date != null ? date : event.getDate()));
        return event;
    }

    // 이벤트 삭제
    @Transactional
    public boolean removeEvent(Long eventId) {
        Event event = eventRepository.findByEventId(eventId);
        boolean result = eventRepository.removeEvent(event);
        if (!result) {
            return false;
        } else {
            return true;
        }
    }

    // 모든 이벤트 조회
    public List<Event> findAllEvents() {
        return eventRepository.findAllEvents();
    }

    // 포스터를 제외한 모든 이벤트 조회(관리자용)
    public List<EventAllRequestExceptImage> findAllEventsExceptImage() {
        return eventRepository.findAllExceptImage();
    }

    // 이벤트 이름으로 이벤트 찾기
    public Event findByEventName(String eventName) {
        Event event = eventRepository.findByEventName(eventName);
        if (event != null) {
            return event;
        } else {
            return null;
        }
    }

    // 이벤트 id로 이벤트 찾기
    public Event findByEventId(Long eventId) {
        Event event = eventRepository.findByEventId(eventId);
        if (event != null) {
            return event;
        } else {
            return null;
        }
    }

    // 내가 참여한 행사 리스트 반환 (id로)
    public List<Event> findAllEvents(String studentId) {
        Member member = memberService.findByStudentId(studentId);
        List<Event> eventList = eventRepository.findAllPartEventsExceptImage(member);

        if (eventList.isEmpty()) {
            return null;
        } else {
            return eventList;
        }
    }

    // (불용) 동명이인 있을 수 있음.
    // 내가 참여한 행사 리스트 반환 (name 으로)
//    public List<Event> findAllEvents(String memberName) {
//        Member member = memberRepository.findByName(memberName);
//        List<Event> eventList = eventRepository.findAllEvents(member);
//        if (eventList != null) {
//            return eventList;
//        } else {
//            return null;
//        }
//    }

//    @Transactional
//    public Event changeEvent (Long eventId, String eventName, String description, MultipartFile logo) throws IOException {
//        Event event = findByEventId(eventId);
//        if (logo != null) {
//            imageBytes = logo.getBytes();
//        } else {
//            imageBytes = club.getLogo();
//        }
//        club.setName(clubName);
//        club.setDescription(description);
//        club.setLogo(imageBytes);
//        return club;
//    }
}
