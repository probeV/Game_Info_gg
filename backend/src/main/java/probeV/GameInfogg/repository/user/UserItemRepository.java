package probeV.GameInfogg.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import probeV.GameInfogg.domain.user.UserItem;
import java.util.List;

@Repository
public interface UserItemRepository extends JpaRepository<UserItem, Long> {

    // 내 아이템 조회
    @Query("SELECT ui FROM UserItem ui JOIN FETCH ui.item WHERE ui.user.id = :userId")
    List<UserItem> findByUserId(Long userId);


}
