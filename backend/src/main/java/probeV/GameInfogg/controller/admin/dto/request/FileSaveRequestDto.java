package probeV.GameInfogg.controller.admin.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FileSaveRequestDto {
    private String directoryPath;

    @Builder
    public FileSaveRequestDto(String directoryPath) {
        this.directoryPath = directoryPath;
    }
}
