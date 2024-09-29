package probeV.GameInfogg.controller.user.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import probeV.GameInfogg.domain.user.UserTask;
import probeV.GameInfogg.domain.task.constant.ModeType;
import probeV.GameInfogg.domain.task.constant.FrequencyType;
import probeV.GameInfogg.domain.task.constant.EventType;
import java.time.DayOfWeek;
import java.time.LocalTime;

@Getter
@NoArgsConstructor
public class UserTaskListResponseDto {

    private Long id;
    private String name;
    private ModeType modeType;
    private FrequencyType frequencyType;
    private EventType eventType;
    private DayOfWeek resetDayOfWeek;
    private LocalTime resetTime;
    private Integer sortPriority;

    public UserTaskListResponseDto(UserTask userTask){
        this.id = userTask.getId();
        this.name = userTask.getName();
        this.modeType = userTask.getModeType();
        this.frequencyType = userTask.getFrequencyType();
        this.eventType = userTask.getEventType();
        this.resetDayOfWeek = userTask.getResetDayOfWeek();
        this.resetTime = userTask.getResetTime();
        this.sortPriority = userTask.getSortPriority();
    }
}


