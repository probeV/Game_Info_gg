package probeV.GameInfogg.controller.admin.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DefaultTaskListDeleteDto {
    private Integer id;

    @Builder
    public DefaultTaskListDeleteDto(Integer id) {
        this.id = id;
    }
}
