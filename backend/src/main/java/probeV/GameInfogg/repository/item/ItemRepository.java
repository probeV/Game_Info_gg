package probeV.GameInfogg.repository.item;

import org.springframework.data.jpa.repository.JpaRepository;
import probeV.GameInfogg.domain.item.Item;

import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    // 아이템 이름 검색
    List<Item> findByNameContaining(String keyword);
}
