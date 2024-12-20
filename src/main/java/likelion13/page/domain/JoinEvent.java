package likelion13.page.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Getter
public class JoinEvent {

    @Id @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "eventId")
    private Event event; // 행사 이름

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "memberId")
    private Member member;

    private LocalDateTime checkDate;

    public JoinEvent(Member member, Event event) {
        this.event = event;
        this.member = member;
        this.checkDate = LocalDateTime.now();
    }
}
