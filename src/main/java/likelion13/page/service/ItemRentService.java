package likelion13.page.service;

import likelion13.page.domain.Item;
import likelion13.page.domain.ItemRent;
import likelion13.page.domain.Member;
import likelion13.page.domain.RentStatus;
import likelion13.page.exception.HavePenaltyException;
import likelion13.page.exception.LimitRentException;
import likelion13.page.exception.MessageException;
import likelion13.page.exception.NotEnoughItemException;
import likelion13.page.repository.ItemRentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static likelion13.page.DTO.ItemRentDTO.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemRentService {

    private final ItemRentRepository itemRentRepository;
    private final MemberService memberService;
    private final ItemService itemService;
    private final DateCheckService dateCheckService;

    //물품은 프론트에서도 그냥 물품번호로 관리해주세요... 이름으로 쿼리할 필요 없을듯..
    //대여예약
    @Transactional
    public BookDTO bookItem(String studentId, long itemId, int count){
        if(count<=0) throw new MessageException("물품 대여 개수가 잘못되었습니다.", HttpStatus.NOT_ACCEPTABLE);

        Member renter = memberService.findByStudentId(studentId);
        Item item = itemService.findById(itemId);

        if(isPenalty(studentId)){
            throw new HavePenaltyException();
        }

        MemberRentingSize memberRentingSize = checkMemberRenting(studentId);
        memberRentingSize.variety.add(itemId);
        if(memberRentingSize.variety.size() > 3){
            throw new LimitRentException("물품은 세 종류까지만 대여가 가능합니다.");
        }
        if(memberRentingSize.count+count > 5){
            throw new LimitRentException("물픔은 최대 5개까지만 대여가 가능합니다.");
        }

        if(count + checkItemBooking(itemId) + item.getRentingCount() > item.getCount()){
            throw new NotEnoughItemException();
        }

        ItemRent itemRent = itemRentRepository.save(new ItemRent(renter, item, count));
        return new BookDTO(itemRent, dateCheckService.needReceiveDate(itemRent.getOfferDate()));
    }

    @Transactional
    public ReceiveDTO receiveItem(long itemRentId){
        ItemRent itemRent = findById(itemRentId);
        itemRent.itemReceive(ItemRent.getNow());
        return new ReceiveDTO(itemRent,dateCheckService.needReturnDate(itemRent.getReceiveDate()));
    }

    @Transactional
    public ItemRent returnItem(long itemRentId){
        ItemRent itemRent = findById(itemRentId);
        LocalDateTime now = ItemRent.getNow();
        LocalDateTime needReturnDate = dateCheckService.needReturnDate(now);
        RentStatus status = RentStatus.RETURN;
        if(now.isAfter(needReturnDate)){
            if(now.isAfter(dateCheckService.needReturnDate(needReturnDate))){
                status = RentStatus.LONG_DELAY_RETURN;
            }else {
                status = RentStatus.DELAY_RETURN;
            }
        }
        itemRent.itemReturn(now, status);
        return itemRent;
    }

    @Transactional
    public Boolean cancelRent(String studentId, long itemRentId){
        ItemRent itemRent = findById(itemRentId);
        Member member = memberService.findByStudentId(studentId);
        if(itemRent.getRenter() != member){
            //권한없음 오류
            return false;
        }
        itemRent.cancel();
        return true;
    }

    @Transactional
    public Boolean adminCancelRent(long itemRentId){
        ItemRent itemRent = findById(itemRentId);
        itemRent.cancel();
        return true;
    }

    public List<BookDTO> memberBookList(String studentId){
        Member member = memberService.findByStudentId(studentId);
        LocalDateTime beforeBuzTime = dateCheckService.beforeBuzDay(ItemRent.getNow().toLocalDate()).atStartOfDay();
        List<ItemRent> bookList = itemRentRepository.findBookWithMember(member, beforeBuzTime);
        return bookList.stream()
                .map(itemRent -> new BookDTO(itemRent, dateCheckService.needReceiveDate(itemRent.getOfferDate())))
                .toList();
    }

    public List<RentDTO> memberRentList(String studentId){
        Member member = memberService.findByStudentId(studentId);
        List<ItemRent> rentList = itemRentRepository.findByMemberStatus(member, Collections.singleton(RentStatus.RENT));
        LocalDateTime now = ItemRent.getNow();
        return rentList.stream()
                .map(itemRent -> new RentDTO(itemRent, dateCheckService.needReturnDate(itemRent.getReceiveDate()), checkDelay(itemRent)))
                .toList();
    }

    public List<AdminBookListDTO> allBookList(){
        LocalDateTime beforeBuzTime = dateCheckService.beforeBuzDay(ItemRent.getNow().toLocalDate()).atStartOfDay();
        return itemRentRepository.findBookWithoutImage(beforeBuzTime);
    }

    public List<AdminRentListDTO> allRentList(){
        List<AdminRentListDTO> rentList = itemRentRepository.findRentWithoutImage();
        LocalDateTime now = ItemRent.getNow();
        for (AdminRentListDTO rent:rentList) {
            rent.setStatus(checkDelay(rent.getRentTime(), now));
        }
        return rentList;
    }

//    public List<?> itemListWithBookingSize(){
//        List<ItemWithBookingSizeDTO> all = itemService.findAll();
//
//    }

    public boolean isPenalty(String studentId){
        Member member = memberService.findByStudentId(studentId);
        int delay = 0;
        int longDelay = 0;
        for (ItemRent itemRent : itemRentRepository.findByMemberStatus(member,RentStatus.canPenaltyGroup)) {
            DelayStatus delayState = checkDelay(itemRent);
            if(delayState == DelayStatus.DELAY){
                delay++;
            }else if(delayState == DelayStatus.LONG_DELAY) {
                longDelay++;
            }
        }
        return delay >= 3 || longDelay >= 1;
    }

    public MemberRentingSize checkMemberRenting(String studentId) {
        Member member = memberService.findByStudentId(studentId);
        Set<Long> set = new HashSet<>();
        int count = 0;
        LocalDateTime beforeBuzTime = dateCheckService.beforeBuzDay(ItemRent.getNow().toLocalDate()).atStartOfDay();
        for (ItemRent itemRent : itemRentRepository.findRentingBooking(member, beforeBuzTime)) {
            set.add(itemRent.getItem().getId());
            count += itemRent.getCount();
        }
        return new MemberRentingSize(set, count);
    }

    public long checkItemBooking(Long itemId){
        Item item = itemService.findById(itemId);
        LocalDateTime beforeBuzTime = dateCheckService.beforeBuzDay(ItemRent.getNow().toLocalDate()).atStartOfDay();
        return itemRentRepository.findBookWithItem(item, beforeBuzTime);
    }

    public List<RestItemListDTO> getrestItemList(){
        LocalDateTime beforeBuzTime = dateCheckService.beforeBuzDay(ItemRent.getNow().toLocalDate()).atStartOfDay();
        return itemRentRepository.findRestItemList(beforeBuzTime);
    }

//    public

    @Transactional
    public Boolean isAutoCancel(ItemRent itemRent){
        if(itemRent.getStatus() != RentStatus.BOOK) return null;

        LocalDateTime now = ItemRent.getNow();
        LocalDateTime needReceiveDate = dateCheckService.needReceiveDate(itemRent.getOfferDate());
        if(now.isAfter(needReceiveDate)){
            itemRent.cancel(); // DateChecker 문제 해결 후 로직 변경시 삭제
            return true;
        }
        return false;
    }

    public DelayStatus checkDelay(ItemRent itemRent){
        if(itemRent.getStatus() == RentStatus.LONG_DELAY_RETURN) return DelayStatus.LONG_DELAY;
        if(itemRent.getStatus() == RentStatus.DELAY_RETURN) return DelayStatus.DELAY;

        LocalDateTime returnTime = ItemRent.getNow();

        if(itemRent.getStatus() == RentStatus.RETURN){
            returnTime = itemRent.getReturnDate();//만약 이미 반납했으면 반납시간으로 변경
        }

        return checkDelay(itemRent.getReceiveDate(), returnTime);
    }

    public DelayStatus checkDelay(LocalDateTime rentTime, LocalDateTime returnTime){

        LocalDateTime needReturnDate = rentTime.with(dateCheckService.needReturnDate(rentTime));
        LocalDateTime longReturnDate = rentTime.with(dateCheckService.needReturnDate(needReturnDate));
        System.out.println("needReturnDate = " + needReturnDate);
        System.out.println("longReturnDate = " + longReturnDate);
        if (returnTime.isAfter(needReturnDate)) {
            if (returnTime.isAfter(longReturnDate)) {
                return DelayStatus.LONG_DELAY;
            } else {
                return DelayStatus.DELAY;
            }
        }
        return DelayStatus.NO_DELAY;
    }

    public ItemRent findById(long itemRentId){
        return itemRentRepository.findById(itemRentId);
    }


}
