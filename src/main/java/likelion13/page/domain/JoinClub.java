package likelion13.page.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
@NoArgsConstructor
public class JoinClub {
    @Id @GeneratedValue
//    @Column(name = "joinclub_id")
    private Long id;

    // Member 엔티티와 Club 엔티티에 대한 참조를 각각 하나씩만 가지므로 단일 참조
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member member;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "club_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Club club;

    public JoinClub(Club club, Member member) {
        this.club = club;
        this.member = member;
    }
}