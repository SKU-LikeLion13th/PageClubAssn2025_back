package likelion13.page.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor
public class DateCheck {
    @Id @GeneratedValue
    private Long id;

    private LocalDate date;

    private LocalDate nextBizDay;
    private LocalDate nextWeekBizDay;
    public DateCheck(LocalDate date, LocalDate nextBizDay, LocalDate nextWeekBizDay){
        this.date = date;
        this.nextBizDay = nextBizDay;
        this.nextWeekBizDay = nextWeekBizDay;
    }
}
