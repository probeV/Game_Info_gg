package probeV.GameInfogg.controller.admin.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import probeV.GameInfogg.domain.task.DefaultTask;
import java.time.DayOfWeek;
@Getter
@NoArgsConstructor
public class DefaultTaskSaveRequestDto {
    private String name;
    private DayOfWeek dayOfWeek;
    private String time;
    
    @Builder
    public DefaultTaskSaveRequestDto(String name, DayOfWeek dayOfWeek, String time) {
        this.name = name;
        this.dayOfWeek = dayOfWeek;
        this.time = time;
    }

    public DefaultTask toEntity() {
        return DefaultTask.builder()
            .name(name)
            .dayOfWeek(dayOfWeek)
            .time(time)
            .build();
    }
}
