package likelion13.page.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import likelion13.page.service.ImageUtility;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;

@Entity
@NoArgsConstructor
@Getter
public class Event {

    @Id @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String name; // 행사 이름

    @Column(nullable = false, name = "poster", columnDefinition = "MEDIUMBLOB") // 아직 어떻게 넣을지 몰라서
    private byte[] poster; // 행사 포스터, 자료형은 미정

    private LocalDate date; // 행사 날짜

    public Event(String name, byte[] poster, LocalDate date) {
        this.name = name;
        this.poster = poster;
        this.date = date;
    }

    public void setPoster(MultipartFile poster) throws IOException {
        this.poster = poster.getBytes();
    }
    public String arrayToImage() {
        return ImageUtility.encodeImage(this.poster);
    }

    public void changeEvent(String name, LocalDate date) {
        this.name = name;
        this.date = date;
    }
}

