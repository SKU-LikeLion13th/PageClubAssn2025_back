package likelion13.page.service;

import likelion13.page.DTO.ItemDTO.*;
import likelion13.page.domain.Item;
import likelion13.page.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemService {
    private final ItemRepository itemRepository;

    public Item findById(Long id){
        return itemRepository.findById(id);
    }

    @Transactional
    public Item save(String name, int count, MultipartFile image) throws IOException{
        byte[] imageBytes = image.getBytes();
        Item item = new Item(name, count, imageBytes);
        return itemRepository.save(item);
    }

    // 관리자 페이지 전용 (물품 관리)
    public List<Item> findAll() {
        return itemRepository.findAll();
    }

    public List<ItemAllRequestExceptImage> findAllExceptImage() {
        return itemRepository.findAllExceptImage();
    }

    @Transactional
    public void delete(Long id) {
        Item item = findById(id);
        item.changeStatus();
    }

    @Transactional
    public Item changeItem(Long itemId, String name, Integer count, MultipartFile image) throws IOException {

        Item item = findById(itemId);
        // 사진 넣으면 바꾼 사진으로
        if (image != null) {
            item.setImage(image);
        }
        String newName = (name != null ? name : item.getName());
        int newCount = (count != null ? count : item.getCount());
        item.changeItem(newName, count);

        return item;
    }

}
