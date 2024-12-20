package likelion13.page.repository;

import jakarta.persistence.EntityManager;
import likelion13.page.domain.Item;
import likelion13.page.domain.ItemRent;
import likelion13.page.domain.Member;
import likelion13.page.domain.RentStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static likelion13.page.DTO.ItemRentDTO.*;

@Repository
@RequiredArgsConstructor
public class ItemRentRepository {
    private final EntityManager em;

    public ItemRent save(ItemRent rentItem){
        em.persist(rentItem);
        return rentItem;
    }

    public ItemRent findById(Long id){
        return em.find(ItemRent.class, id);
    }

    public List<ItemRent> findByMember(Member member){
        return em.createQuery("select ir from ItemRent ir where ir.renter = :member", ItemRent.class)
                .setParameter("member",member).getResultList();
    }

    public List<ItemRent> findByItem(Item item){
        return em.createQuery("select ir from ItemRent ir where ir.item = :item", ItemRent.class)
                .setParameter("item",item).getResultList();
    }

    public List<ItemRent> findByStatus(RentStatus status){
        return em.createQuery("select ir from ItemRent ir where ir.status = :status", ItemRent.class)
                .setParameter("status",status).getResultList();
    }
//
//    public List<ItemRent> findByMemberStatus(Member member, RentStatus status){
//        return em.createQuery("select ir from ItemRent ir where ir.renter = :member and ir.status = :status", ItemRent.class)
//                .setParameter("status",status).setParameter("member", member).getResultList();
//    }

    public List<ItemRent> findByMemberStatus(Member member, Set<RentStatus> statusGroup) {
        return em.createQuery("SELECT ir FROM ItemRent ir WHERE ir.renter = :member AND ir.status IN :statusGroup", ItemRent.class)
                .setParameter("member", member)
                .setParameter("statusGroup", statusGroup)
                .getResultList();
    }

    public List<ItemRent> findRentingBooking(Member member, LocalDateTime localDateTime) {
        return em.createQuery("select ir from ItemRent ir where ir.renter = :member and ((ir.status = :book and ir.offerDate >= :time) or ir.status = :rent)", ItemRent.class)
                .setParameter("member",member).setParameter("book", RentStatus.BOOK).setParameter("time",localDateTime).setParameter("rent",RentStatus.RENT)
                .getResultList();
    }


//    public List<ItemRent> findByItemStatus(Item item, RentStatus status){
//        return em.createQuery("select ir from ItemRent ir where ir.item = :item and ir.status = :status", ItemRent.class)
//                .setParameter("status",status).setParameter("item", item).getResultList();
//    }

    public List<ItemRent> findByItemStatus(Item item, Set<RentStatus> statusGroup) {
        return em.createQuery("SELECT ir FROM ItemRent ir WHERE ir.item = :item AND ir.status IN :statusGroup", ItemRent.class)
                .setParameter("item", item)
                .setParameter("statusGroup", statusGroup)
                .getResultList();
    }


    public List<ItemRent> findAll(){
        return em.createQuery("select ir from ItemRent ir", ItemRent.class).getResultList();
    }

//    public List<ItemRent> findMemberRenting(Member member){
//        return em.createQuery("select ir from ItemRent ir where ir.renter = :member and (ir.status = :book or ir.status = :rent)", ItemRent.class)
//                .setParameter("member",member).setParameter("book",RentStatus.BOOK).setParameter("rent",RentStatus.RENT).getResultList();
//    }

//    public List<ItemRent> findItemRenting(Item item){
//        return em.createQuery("select ir from ItemRent ir where ir.item = :item and (ir.status = :book or ir.status = :rent)", ItemRent.class)
//                .setParameter("item",item).setParameter("book",RentStatus.BOOK).setParameter("rent",RentStatus.RENT).getResultList();
//    }

    public Long findBookWithItem(Item item, LocalDateTime localDateTime){
        return em.createQuery("select COALESCE(SUM(ir.count), 0) from ItemRent ir where ir.item = :item and ir.offerDate >= :localDateTime and ir.status = :status",Long.class)
                .setParameter("item",item).setParameter("localDateTime",localDateTime).setParameter("status",RentStatus.BOOK)
                .getSingleResult();
    }

    public List<ItemRent> findBookWithMember(Member member, LocalDateTime localDateTime){
        return em.createQuery("select ir from ItemRent ir where ir.renter = :member and ir.status = :status and ir.offerDate >= :time", ItemRent.class)
                .setParameter("member",member).setParameter("status", RentStatus.BOOK).setParameter("time",localDateTime)
                .getResultList();
    }

    public List<AdminBookListDTO> findBookWithoutImage(LocalDateTime localDateTime){
        return em.createQuery("select new AdminBookListDTO(ir.id, ir.renter.studentId, ir.renter.name, ir.item.name, ir.renterClub, ir.count, ir.offerDate) " +
                        "from ItemRent ir where ir.status = :status and ir.offerDate >= :time", AdminBookListDTO.class)
                .setParameter("status", RentStatus.BOOK).setParameter("time",localDateTime)
                .getResultList();
    }

    public List<AdminRentListDTO> findRentWithoutImage(){
        return em.createQuery("select new AdminRentListDTO(ir.id, ir.renter.studentId, ir.renter.name, ir.item.name, ir.renterClub, ir.count, ir.receiveDate) " +
                        "from ItemRent ir where ir.status = :status", AdminRentListDTO.class)
                .setParameter("status", RentStatus.RENT)
                .getResultList();
    }

    public List<RestItemListDTO> findRestItemList(LocalDateTime localDateTime){
        return em.createQuery("SELECT new RestItemListDTO(i.id, i.name, i.count, i.image, i.rentingCount, COALESCE(SUM(ir.count), 0)) " +
                                "FROM ItemRent ir RIGHT JOIN Item i ON i = ir.item and ir.offerDate >= :localDateTime and ir.status = :booking where i.isActive = true GROUP BY i", RestItemListDTO.class)
                .setParameter("booking", RentStatus.BOOK)
                .setParameter("localDateTime",localDateTime).getResultList();
    }

    public void delete(ItemRent itemRent){
        em.remove(itemRent);
    }



}
