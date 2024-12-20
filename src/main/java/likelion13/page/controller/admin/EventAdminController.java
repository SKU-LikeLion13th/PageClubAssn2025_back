package likelion13.page.controller.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import likelion13.page.DTO.EventDTO.*;
import likelion13.page.domain.Event;
import likelion13.page.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;



@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/events")
@Tag(name = "관리자 페이지: 퍼즐조각 관리 관련")
public class EventAdminController {
    private final EventService eventService;

    // 이벤트 추가
    @Operation(summary = "(택원) 관리자가 새로운 퍼즐 조각 추가하는 API", description = "퍼즐 이름, 퍼즐 이미지, 퍼즐 행사 날짜 받아야함")
    @PostMapping("/add")
    public ResponseEntity<ResponseEvent> addEvent(RequestEvent request) throws IOException {
        Event event = eventService.addEvent(request.getName(), request.getImage(), request.getDate());
        ResponseEvent responseEvent = new ResponseEvent(event.getId(), event.getName(), event.arrayToImage(), event.getDate());
        return ResponseEntity.ok(responseEvent);
    }

    //이벤트 리스트 조회 추가 필요
    @Operation(summary = "(민규) 모든 이벤트 조회", description = "모든 이벤트를 조회하여 이벤트 포스터를 제외한 이벤트 이름, 이벤트 일자 조회")
    @GetMapping("/all")
    public ResponseEntity<List<EventAllRequestExceptImage>> findAllEvents() {
        List<Event> events = eventService.findAllEvents();
        List<EventAllRequestExceptImage> eventDTOs = new ArrayList<>();

        for (Event event : events) {
            EventAllRequestExceptImage dto = new EventAllRequestExceptImage(event.getId(), event.getName(), event.getDate());
            eventDTOs.add(dto);
        }
        return ResponseEntity.status(HttpStatus.OK).body(eventDTOs);
    }

    //개별 리스트 조회는 만들지 않을 예정

    // 퍼즐 조각 수정 페이지
//    @GetMapping("/events/manage/update")
//    public ResponseEntity<ResponseEvent> eventUpdatePage(@RequestBody RequestEvent request) {
//
//    }
//
    // 퍼즐 조각 수정 기능
    @Operation(summary = "(민규) 관리자가 퍼즐 조각 수정",
            description = "이벤트id, 수정하고자하는 이름, 날짜, 사진 입력. 넣지 않은 항목은 원래 값으로 들어감.")
    @PutMapping("/update")
    public ResponseEntity<ResponseEvent> updateEvent(UpdateRequestEvent request) throws IOException {
        Event event = eventService.changeEvent(request.getId(), request.getName(), request.getDate(), request.getImage());
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseEvent(event.getId(), event.getName(), event.arrayToImage(), event.getDate()));
    }
    // 퍼즐 조각 삭제
    @Operation(summary = "(민규) 관리자가 퍼즐 조각 삭제하는 API", description = "퍼즐 id 입력")
    @DeleteMapping("")
    public void deleteEvent(@RequestBody DeleteEvents request) {
        eventService.removeEvent(request.getId());
    }
}