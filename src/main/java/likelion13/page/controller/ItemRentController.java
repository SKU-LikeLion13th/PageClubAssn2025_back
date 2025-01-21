package likelion13.page.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import likelion13.page.security.JwtUtility;
import likelion13.page.service.ItemRentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static likelion13.page.DTO.ItemRentDTO.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/item-rent")
@Tag(name = "멤버 페이지: 물품 대여 관련")
public class ItemRentController {

    private final ItemRentService itemRentService;
    private final JwtUtility jwtUtility;

    @Operation(summary = "물품 대여창 목록 출력용(가나다 정렬) ", description = "대여중, 예약중 개수를 포함하여 물품의 목록을 조회")
    @GetMapping("/list")
    public ResponseEntity<List<RestItemListDTO>> restItemList() {
        return ResponseEntity.status(HttpStatus.OK).body(itemRentService.getrestItemList());
    }

    @Operation(summary = "(민규) 물품 대여 예약", description = "토큰, 물품번호, 대여시도 개수 필요",
            responses = {@ApiResponse(responseCode = "200", description = "대여 성공"),
                    @ApiResponse(responseCode = "403", description = "반납 3회이상"),
                    @ApiResponse(responseCode = "403", description = "미반납 1회이상")
            })
    @PostMapping("")
    public ResponseEntity<BookDTO> bookRequest(HttpServletRequest header, @RequestBody BookRequestDTO request) {
        BookDTO bookDTO = itemRentService.bookItem(jwtUtility.getStudentId(jwtUtility.resolveToken(header)), request.getItemId(), request.getCount());
        return ResponseEntity.status(HttpStatus.OK).body(bookDTO);
    }

    @Operation(summary = "물품 대여 예약 취소", description = "토큰, 대여번호 필요",
            responses = {@ApiResponse(responseCode="200", description="예약 취소 성공")
            })
    @DeleteMapping("")
    public ResponseEntity<?> cancelItem(HttpServletRequest header, @RequestBody CancelRequestDTO request){
        itemRentService.cancelRent(jwtUtility.getStudentId(jwtUtility.resolveToken(header)),request.getItemRentId());
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Operation(summary = "특정 멤버가 예약중인 물품 리스트", description = "헤더에 토큰 필요")
    @GetMapping("/book-list")
    public ResponseEntity<List<ReservedDTO>> memberBookList(HttpServletRequest header){
        List<ReservedDTO> list = itemRentService.memberBookList(jwtUtility.getStudentId(jwtUtility.resolveToken(header)));
        return ResponseEntity.status(HttpStatus.OK).body(list);
    }

    @Operation(summary = "특정 멤버가 대여중인 물품 리스트", description = "헤더에 토큰 필요")
    @GetMapping("/rent-list")
    public ResponseEntity<List<RentDTO>> memberRentList(HttpServletRequest header){
        List<RentDTO> list = itemRentService.memberRentList(jwtUtility.getStudentId(jwtUtility.resolveToken(header)));
        return ResponseEntity.status(HttpStatus.OK).body(list);
    }
}
