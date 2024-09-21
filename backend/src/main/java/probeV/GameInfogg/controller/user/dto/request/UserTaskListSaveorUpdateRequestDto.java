package probeV.GameInfogg.controller.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import probeV.GameInfogg.domain.task.DefaultTask;
import probeV.GameInfogg.domain.task.constant.EventType;
import probeV.GameInfogg.domain.task.constant.FrequencyType;
import probeV.GameInfogg.domain.task.constant.ModeType;
import probeV.GameInfogg.domain.task.UserTask;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class UserTaskListSaveorUpdateRequestDto {
    private Long id;

    @NotBlank(message = "이름은 필수 입력 항목입니다.")
    private String name;

    @NotBlank(message = "모드는 필수 입력 항목입니다.")
    private String mode;

    @NotBlank(message = "주기는 필수 입력 항목입니다.")
    private String frequency;

    @NotBlank(message = "이벤트는 필수 입력 항목입니다.")
    private String event;

    private String resetDayOfWeek;

    private String resetTime;

    private Integer sortPriority;

    @Builder
    public UserTaskListSaveorUpdateRequestDto(Long id, String name, String mode, String frequency, String event, String resetDayOfWeek, String resetTime, Integer sortPriority) {
        this.id = id;
        this.name = name;
        this.mode = mode;
        this.frequency = frequency;
        this.event = event;
        this.resetDayOfWeek = resetDayOfWeek;
        this.resetTime = resetTime;
        this.sortPriority = sortPriority;
    }

    public UserTask toEntity() {
        return UserTask.builder()
            .name(name)
            .resetDayOfWeek(resetDayOfWeek != null ? DayOfWeek.valueOf(resetDayOfWeek) : null)
            .resetTime(resetTime != null ? LocalTime.parse(resetTime) : null)
            .sortPriority(sortPriority)
            .modeType(ModeType.fromString(mode))
            .frequencyType(FrequencyType.fromString(frequency))
            .eventType(EventType.fromString(event))
            .build();
    }



}
