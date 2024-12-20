package likelion13.page.domain;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Entity
@Getter
@NoArgsConstructor
public class ItemRent {

    @Id @GeneratedValue @Column(name = "rent_item_id")
    private Long id;

    @ManyToOne @JoinColumn(name="renter_id")
    private Member renter;
    @ManyToOne @JoinColumn(name = "item_id")
    private Item item;

    private Integer count;

    @Column(name = "rent_offer_date")
    private LocalDateTime offerDate;
    @Column(name = "rent_start_date")
    private LocalDateTime receiveDate;
    @Column(name = "rent_return_date")
    private LocalDateTime returnDate;

    @Enumerated(EnumType.STRING)
    private RentStatus status;

    private String renterClub;

//    private TimeStatus tStatus;//해당없읍, 지연, 장기지연

    public static final long longDelayTime = (7 * 24 * 60 * 60 * 1000);

    public ItemRent(Member member, Item item, int count){
        this.renter = member;
        this.item = item;
        this.count = count;
        this.offerDate = getNow();
        this.status = RentStatus.BOOK;
        if(member.getIconClub()!=null) {
            this.renterClub = member.getIconClub().getName();
        }else{
            this.renterClub = "소속없음";
        }
    }

    public void cancel(){
        this.status = RentStatus.CANCEL;
    }

    public void itemReceive(LocalDateTime receiveTime){
        this.receiveDate = receiveTime;
        this.status = RentStatus.RENT;
        this.item.rentItem(this.count);
    }

    public void itemReturn(LocalDateTime returnTime, RentStatus status){
        this.returnDate = returnTime;
        this.status = status;
        this.item.returnItem(this.count);
    }

    public static LocalDateTime getNow(){
        return LocalDateTime.now(ZoneId.of("Asia/Seoul"));
    }

}
