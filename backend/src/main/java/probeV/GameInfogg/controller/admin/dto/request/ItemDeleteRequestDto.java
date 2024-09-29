package probeV.GameInfogg.controller.admin.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Builder;
@Getter
@NoArgsConstructor
public class ItemDeleteRequestDto {
    private String imageUrl;

    @Builder
    public ItemDeleteRequestDto(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
