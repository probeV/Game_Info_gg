package probeV.GameInfogg.controller.task.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import probeV.GameInfogg.domain.task.DefaultTask;
import probeV.GameInfogg.domain.task.constant.FrequencyType;
import probeV.GameInfogg.domain.task.constant.ModeType;
import probeV.GameInfogg.domain.task.constant.EventType;



@Getter
@NoArgsConstructor
public class DefaultTaskListResponseDto {

    private Integer id;
    private String name;
    private ModeType modeType;
    private FrequencyType frequencyType;
    private EventType eventType;

    public DefaultTaskListResponseDto(DefaultTask defaultTask) {
        this.id = defaultTask.getId();
        this.name = defaultTask.getName();
        this.modeType = defaultTask.getModeType();
        this.frequencyType = defaultTask.getFrequencyType();
        this.eventType = defaultTask.getEventType();
    }
}
