package probeV.GameInfogg.controller.admin.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DefaultTaskListDeleteRequestDto {
    private Integer id;

    @Builder
    public DefaultTaskListDeleteRequestDto(Integer id) {
        this.id = id;
    }
}
