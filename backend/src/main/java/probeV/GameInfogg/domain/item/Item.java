package probeV.GameInfogg.domain.item;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import probeV.GameInfogg.domain.user.UserItem;

import java.util.ArrayList;
import java.util.List;

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

    @Column(name = "effect", nullable = false)
    private String effect;

    @Column(name = "description", nullable = false)
    private String description;

    @Builder
    public Item(String name, String imageUrl, String effect, String description) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.effect = effect;
        this.description = description;
    }

    public void update(String name, String effect, String description, String imageUrl) {
        this.name = name;
        this.effect = effect;
        this.description = description;
        this.imageUrl = imageUrl;
    }

}
