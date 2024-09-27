package probeV.GameInfogg.controller.admin.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FileUpdateRequestDto {
    private String preFileUrl;
    private String directoryPath;

    @Builder
    public FileUpdateRequestDto(String preFileUrl, String directoryPath) {
        this.preFileUrl = preFileUrl;
        this.directoryPath = directoryPath;
    }
}
