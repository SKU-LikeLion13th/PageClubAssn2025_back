package likelion13.page.domain;

import jakarta.persistence.*;
import likelion13.page.service.ImageUtility;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Item {

    @Id @GeneratedValue @Column(name="item_id")
    private Long id;

    private String name;
    private Integer count;
    private Integer rentingCount;

    // 이미지를 담는 byte 배열을 BLOB(Binary Large Object) 형식으로 저장
    @Lob
    @Column(name = "image", columnDefinition = "MEDIUMBLOB") // TINYBLOB: ~255Byte BLOB: ~64KB  MEDIUMBLOB: ~16MB LONGBLOB: ~4GB
    private byte[] image;

    private boolean isActive;

    public Item(String name, int count, byte[] image){
        this.name = name;
        this.count = count;
        this.image = image;
        this.rentingCount = 0;
        this.isActive = true;
    }

    public void changeItem(String name, Integer count) {
        this.name = name;
        this.count = count;
    }
    public void rentItem(int num){
        rentingCount += num;
    }

    public void returnItem(int num){
        rentingCount -= num;
    }
    public String arrayToImage() {
        return ImageUtility.encodeImage(this.image);
    }

    public void setImage(MultipartFile image) throws IOException {
        this.image = image.getBytes();
    }

    public void changeStatus(){
        this.isActive = false;
    }
}
