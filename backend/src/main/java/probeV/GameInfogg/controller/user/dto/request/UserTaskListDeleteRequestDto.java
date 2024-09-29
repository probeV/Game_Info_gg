package probeV.GameInfogg.controller.user.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter

@NoArgsConstructor
public class UserTaskListDeleteRequestDto {
    private Long userTaskId;

    @Builder
    public UserTaskListDeleteRequestDto(Long userTaskId) {
        this.userTaskId = userTaskId;
    }
}

