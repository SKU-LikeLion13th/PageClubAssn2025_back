package likelion13.page.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import likelion13.page.DTO.ItemDTO.*;
import likelion13.page.domain.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ItemRepository {
    private final EntityManager em;

    public Item save(Item item){
        em.persist(item);
        return item;
    }

    public void delete(Item item) {
        em.remove(item);
    }

    public Item findById(Long id){
        return em.find(Item.class, id);
    }

    public List<Item> findAll(){
        return em.createQuery("select i from Item i", Item.class).getResultList();
    }

    public List<ItemAllRequestExceptImage> findAllExceptImage() {
        return em.createQuery("SELECT new ItemAllRequestExceptImage(i.id, i.name, i.count) FROM Item i WHERE i.isActive=true", ItemAllRequestExceptImage.class)
                .getResultList();
    }
}
