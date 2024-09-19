package probeV.GameInfogg.controller.admin.dto.request;

import java.time.DayOfWeek;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DefaultTaskUpdateDto {
    private String name;
    private DayOfWeek dayOfWeek;
    private String time;

    @Builder
    public DefaultTaskUpdateDto(String name, DayOfWeek dayOfWeek, String time) {
        this.name = name;
        this.dayOfWeek = dayOfWeek;
        this.time = time;
    }

}
