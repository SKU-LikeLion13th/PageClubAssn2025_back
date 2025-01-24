package likelion13.page.controller.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import likelion13.page.DTO.ItemRentDTO.AdminBookListDTO;
import likelion13.page.DTO.ItemRentDTO.AdminRentListDTO;
import likelion13.page.DTO.ItemRentDTO.RequestItemRent;
import likelion13.page.service.ItemRentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/item-rent")
@Tag(name = "관리자 페이지: 물품대여 관리 관련")
public class ItemRentAdminController {

    private final ItemRentService itemRentService;

    @Operation(summary = "물품 예약 리스트", description = "",
            parameters = {
                    @Parameter(name = "Authorization", description = "Access Token", required = true, in = ParameterIn.HEADER, schema = @Schema(implementation = String.class), example = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkxvcmV0IiwiaWF0IjoxNjQ0NTcwODAwLCJleHAiOjE2NDQ1NzQ0MDB9.sN5z7tC035_7207f-1042_41042_41042_41042_41042_41042_41042_41042_41042")
            })
    @GetMapping("/book-list")
    public ResponseEntity<List<AdminBookListDTO>> itemBookList(){
        List<AdminBookListDTO> adminBookList = itemRentService.allBookList();
        return ResponseEntity.status(HttpStatus.OK).body(adminBookList);
    }

    @Operation(summary = "물품 대여중 리스트", description = "")
    @GetMapping("/rent-list")
    public ResponseEntity<List<AdminRentListDTO>> itemRentList(){
        List<AdminRentListDTO> adminRentList = itemRentService.allRentList();
        return ResponseEntity.status(HttpStatus.OK).body(adminRentList);
    }

    @Operation(summary = "물품 수령", description = "body에 json 형태로 물품 예약 id 필요")
    @PostMapping("")
    public ResponseEntity<?> itemReceive(@RequestBody RequestItemRent request){
        itemRentService.receiveItem(request.getItemRentId());
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Operation(summary = "물품 반환", description = "body에 json 형태로 물품 예약 id 필요")
    @PutMapping("")
    public ResponseEntity<?> itemReturn(@RequestBody RequestItemRent request){
        itemRentService.returnItem(request.getItemRentId());
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Operation(summary = "예약 취소", description = "body에 json 형태로 물품 예약 id 필요")
    @DeleteMapping("")
    public ResponseEntity<?> cancelRent(@RequestBody RequestItemRent request){
        itemRentService.adminCancelRent(request.getItemRentId());
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
