package probeV.GameInfogg.domain.item;

import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

import probeV.GameInfogg.domain.user.UserItem;

@Getter @Setter
@NoArgsConstructor
@Table(name = "ITEMS")
@Entity
public class Item {
    /* PK */
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /* Relation */
    @OneToMany(mappedBy = "item")
    private List<UserItem> userItems = new ArrayList<>();

    /* Attribute */
    @Column(name = "name", nullable = false)
    private String name;
    
    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "effect")
    private String effect;

    @Column(name = "description")
    private String description;

    @Builder
    public Item(String name, String imageUrl, String effect, String description) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.effect = effect;
        this.description = description;
    }
}
