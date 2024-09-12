package probeV.GameInfogg.controller.task.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import probeV.GameInfogg.domain.task.Task;
import probeV.GameInfogg.domain.task.constant.FrequencyType;
import probeV.GameInfogg.domain.task.constant.ModeType;
import probeV.GameInfogg.domain.task.constant.TaskType;

import java.time.DayOfWeek;

@Getter
@NoArgsConstructor
public class TaskListResponseDto {

    private String name;
    private ModeType modeType;
    private FrequencyType frequencyType;
    private TaskType taskType;
    private DayOfWeek dayOfWeek;
    private String time;

    public TaskListResponseDto(Task task) {
        this.name = task.getName();
        this.modeType = task.getModeType();
        this.frequencyType = task.getFrequencyType();
        this.taskType = task.getTaskType();
        this.dayOfWeek = task.getDayOfWeek();
        this.time = task.getTime();
    }
}
