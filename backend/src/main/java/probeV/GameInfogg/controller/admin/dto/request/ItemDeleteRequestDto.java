package probeV.GameInfogg.controller.admin.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Builder;
@Getter
@NoArgsConstructor
public class ItemDeleteRequestDto {
    private String url;

    @Builder
    public ItemDeleteRequestDto(String url) {
        this.url = url;
    }
}
