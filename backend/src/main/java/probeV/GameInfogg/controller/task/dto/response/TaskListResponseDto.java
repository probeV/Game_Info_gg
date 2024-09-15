package probeV.GameInfogg.controller.task.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import probeV.GameInfogg.domain.task.Task;
import probeV.GameInfogg.domain.task.constant.FrequencyType;
import probeV.GameInfogg.domain.task.constant.ModeType;
import probeV.GameInfogg.domain.task.constant.EventType;

import java.time.DayOfWeek;

@Getter
@NoArgsConstructor
public class TaskListResponseDto {

    private Integer id;
    private String name;
    private ModeType modeType;
    private FrequencyType frequencyType;
    private EventType eventType;
    private DayOfWeek dayOfWeek;
    private String time;

    public TaskListResponseDto(Task task) {
        this.id = task.getId();
        this.name = task.getName();
        this.modeType = task.getModeType();
        this.frequencyType = task.getFrequencyType();
        this.eventType = task.getEventType();
        this.dayOfWeek = task.getDayOfWeek();
        this.time = task.getTime();
    }
}
