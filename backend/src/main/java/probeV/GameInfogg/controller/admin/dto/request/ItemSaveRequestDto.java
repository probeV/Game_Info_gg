package probeV.GameInfogg.controller.admin.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import probeV.GameInfogg.domain.item.Item;

@Getter
@NoArgsConstructor
public class ItemSaveRequestDto {
    @NotBlank(message = "이름은 필수 입력 항목입니다.")
    private String name;
    private String effect;
    private String description;
    private String imageUrl;

    @Builder
    public ItemSaveRequestDto(String name, String effect, String description, String imageUrl) {
        this.name = name;
        this.effect = effect;
        this.description = description;
        this.imageUrl = imageUrl;
    }

    public Item toEntity() {
        return Item.builder()
            .name(name)
            .effect(effect)
            .description(description)
            .imageUrl(imageUrl)
            .build();
    }
}
