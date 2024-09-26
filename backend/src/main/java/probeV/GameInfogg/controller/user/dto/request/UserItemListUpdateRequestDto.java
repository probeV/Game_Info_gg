package probeV.GameInfogg.controller.user.dto.request;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import probeV.GameInfogg.domain.user.UserItem;

import lombok.Builder;
import jakarta.validation.constraints.NotBlank;

@Getter @Setter
@NoArgsConstructor
public class UserItemListUpdateRequestDto {
    private Long userItemid;
    @NotBlank(message = "남은 시간은 필수 입력 항목입니다.")
    private String restTime;

    @Builder
    public UserItemListUpdateRequestDto(Long userItemid, String restTime) {
        this.userItemid = userItemid;
        this.restTime = restTime;
    }

    public UserItem toEntity() {
        return UserItem.builder()
            .restTime(LocalDateTime.parse(restTime))
            .build();
    }
}
