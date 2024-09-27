package probeV.GameInfogg.controller.admin.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ItemUpdateRequestDto {
    private String name;
    private String effect;
    private String description;
    private String preImageUrl;

    @Builder
    public ItemUpdateRequestDto(String name, String effect, String description, String preImageUrl) {
        this.name = name;
        this.effect = effect;
        this.description = description;
        this.preImageUrl = preImageUrl;
    }

}
