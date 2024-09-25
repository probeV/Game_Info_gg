package probeV.GameInfogg.controller.item.dto.response;

@Getter
@NoArgsConstructor
public class ItemListResponseDto {
    private Long id;
    private String name;
    private String imageUrl;
    private String effect;
    private String description;

    public ItemListResponseDto(Item entity) {
        this.id = entity.getId();
        this.name = entity.getName();
        this.imageUrl = entity.getImageUrl();
        this.effect = entity.getEffect();
        this.description = entity.getDescription();
    }
}
