package probeV.GameInfogg.controller.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import probeV.GameInfogg.domain.user.UserItem;

import lombok.Builder;
import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor
public class UserItemListSaveRequestDto {
    @NotBlank(message = "아이템 ID는 필수 입력 항목입니다.")
    private Long itemId;
    @NotBlank(message = "남은 시간은 필수 입력 항목입니다.")
    private String restTime;

    @Builder
    public UserItemListSaveRequestDto(Long itemId, String restTime) {
        this.itemId = itemId;
        this.restTime = restTime;
    }

    public UserItem toEntity() {
        return UserItem.builder()
            .restTime(LocalDateTime.parse(restTime))
            .build();
    }
}
