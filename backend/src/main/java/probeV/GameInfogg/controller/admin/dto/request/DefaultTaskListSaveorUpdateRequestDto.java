package probeV.GameInfogg.controller.admin.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import probeV.GameInfogg.domain.task.DefaultTask;
import probeV.GameInfogg.domain.task.constant.EventType;
import probeV.GameInfogg.domain.task.constant.ModeType;
import probeV.GameInfogg.domain.task.constant.FrequencyType;


@Getter
@NoArgsConstructor
public class DefaultTaskListSaveorUpdateRequestDto {
    private Integer id;

    @NotBlank(message = "이름은 필수 입력 항목입니다.")
    private String name;

    @NotBlank(message = "모드는 필수 입력 항목입니다.")
    private String mode;

    @NotBlank(message = "주기는 필수 입력 항목입니다.")
    private String frequency;

    @NotBlank(message = "이벤트는 필수 입력 항목입니다.")
    private String event;
    
    @Builder
    public DefaultTaskListSaveorUpdateRequestDto(Integer id, String name, String mode, String frequency, String event) {
        this.id = id;
        this.name = name;
        this.mode = mode;
        this.frequency = frequency;
        this.event = event;
    }

    public DefaultTask toEntity() {
        return DefaultTask.builder()
            .name(name)
            .modeType(ModeType.fromString(mode))
            .frequencyType(FrequencyType.fromString(frequency))
            .eventType(EventType.fromString(event))
            .build();
    }
}
