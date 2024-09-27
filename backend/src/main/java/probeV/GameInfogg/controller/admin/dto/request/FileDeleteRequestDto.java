package probeV.GameInfogg.controller.admin.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FileDeleteRequestDto {
    private String imageUrl;

    @Builder
    public FileDeleteRequestDto(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
