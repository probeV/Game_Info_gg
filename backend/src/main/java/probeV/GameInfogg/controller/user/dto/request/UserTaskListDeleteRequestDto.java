package probeV.GameInfogg.controller.user.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter

@NoArgsConstructor
public class UserTaskListDeleteRequestDto {
    private Long id;

    @Builder
    public UserTaskListDeleteRequestDto(Long id) {
        this.id = id;
    }
}

