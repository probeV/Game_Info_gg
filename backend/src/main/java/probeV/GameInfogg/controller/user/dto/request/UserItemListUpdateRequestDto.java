package probeV.GameInfogg.controller.user.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import probeV.GameInfogg.domain.user.UserItem;

import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor
public class UserItemListUpdateRequestDto {
    private Long userItemId;
    private String resetTime;

    @Builder
    public UserItemListUpdateRequestDto(Long userItemId, String resetTime) {
        this.userItemId = userItemId;
        this.resetTime = resetTime;
    }

    public UserItem toEntity() {
        return UserItem.builder()
            .restTime(LocalDateTime.parse(resetTime))
            .build();
    }
}
