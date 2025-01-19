package likelion13.page.controller.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import likelion13.page.DTO.ItemDTO.ItemAllRequestExceptImage;
import likelion13.page.DTO.ItemDTO.ItemCreateRequest;
import likelion13.page.DTO.ItemDTO.ItemCreateResponse;
import likelion13.page.DTO.ItemDTO.ItemUpdateRequest;
import likelion13.page.domain.Item;
import likelion13.page.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/item")
@Tag(name = "관리자 페이지: 물품대여 관리 관련")
public class ItemController {

    private final ItemService itemService;

    // 물품 추가
    @Operation(summary = "관리자가 대여 물품 추가하는 API", description = "물품명, 물품 개수, 물품 이미지 삽입")
    @PostMapping("") //, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> addItem(ItemCreateRequest reqest)  throws IOException {

        itemService.save(reqest.getName(), reqest.getCount(), reqest.getImage());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // 물품 수정(이미지 안바꾸고 싶으면 안넣으면 됨)
    @Operation(summary = "관리자가 대여 물품 수정하는 API1",
            description = "물품id, 수정하고자하는 이름, 사진 입력. 넣지 않은 항목은 원래 값으로 들어감.<br>개수는 입력해줘야함")
    @PutMapping("")
    public ResponseEntity<ItemCreateResponse> changeItem(ItemUpdateRequest request)  throws IOException  {
        Item item = itemService.changeItem(request.getItemId(), request.getName(), request.getCount(), request.getImage());
        return ResponseEntity.status(HttpStatus.CREATED).body(new ItemCreateResponse(item.getName(), item.getCount(), item.arrayToImage()));
    }

    @Operation(summary = "물품 1개 정보 확인하는 API", description = "물품의 id입력")
    @GetMapping("/{itemId}")
    public ResponseEntity<ItemCreateResponse> findOneItem(@PathVariable("itemId") Long itemId) {
        Item item = itemService.findById(itemId);
        return ResponseEntity.status(HttpStatus.OK).body(new ItemCreateResponse(item.getName(), item.getCount(), item.arrayToImage()));
    }

    @Operation(summary = "관리자가 대여 물품 삭제하는 API", description = "물품의 id입력")
    @DeleteMapping("/{itemId}")
    public ResponseEntity<?> deleteItem(@PathVariable Long itemId) {
        itemService.delete(itemId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }


    @Operation(summary = "관리자가 모든 대여 물품 조회하는 API (이미지 제외)", description = "이미지를 제외한 물품 정보 반환")
    @GetMapping("/all")
    public ResponseEntity<List<ItemAllRequestExceptImage>> findAllItemsExceptImage() {
        return ResponseEntity.status(HttpStatus.OK).body(itemService.findAllExceptImage()); // DTO로 쿼리 생성하기. hellospring => findUserAll() 참고
    }
}