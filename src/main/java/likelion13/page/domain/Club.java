package likelion13.page.domain;

import jakarta.persistence.*;
import likelion13.page.service.ImageUtility;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Club {
    @Id @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String name;
    private String description;

    // 이미지를 담는 byte 배열을 BLOB(Binary Large Object) 형식으로 저장
    @Lob
    @Column(name = "logo", columnDefinition = "MEDIUMBLOB") // TINYBLOB: ~255Byte BLOB: ~64KB  MEDIUMBLOB: ~16MB LONGBLOB: ~4GB
    private byte[] logo;

    public Club(String clubName, String description, byte[] logo) {
        this.name = clubName;
        this.description = description;
        this.logo = logo;
    }

    public void setLogo(MultipartFile logo) throws IOException {
        this.logo = logo.getBytes();
    }

    public void changeClub(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String arrayToImage() {
        return ImageUtility.encodeImage(this.logo);
    }
}