package probeV.GameInfogg.domain.user;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import probeV.GameInfogg.domain.item.Item;

import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor
@Table(name = "USERS_ITEMS")
@Entity
public class UserItem {
    /* PK */
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
    /* Relation */
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;
    
    /* Attribute */
    @Column(name = "reset_time", nullable = false)
    private LocalDateTime resetTime;

    @Builder
    public UserItem(LocalDateTime resetTime) {
        this.resetTime = resetTime;
    }

    public void setUser(User user) {
        this.user = user;
        user.getUserItems().add(this);
    }

    public void setItem(Item item) {
        this.item = item;
        item.getUserItems().add(this);
    }

    public void updateUserItem(LocalDateTime restTime) {
        this.resetTime = restTime;
    }
}
