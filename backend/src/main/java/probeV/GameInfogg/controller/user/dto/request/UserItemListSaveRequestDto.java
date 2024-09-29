package probeV.GameInfogg.controller.user.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import probeV.GameInfogg.domain.user.UserItem;

import java.time.LocalDateTime;

@Slf4j
@Getter @Setter
@NoArgsConstructor
public class UserItemListSaveRequestDto {
    private Long itemId;
    private String resetTime;

    @Builder
    public UserItemListSaveRequestDto(Long itemId, String resetTime) {
        this.itemId = itemId;
        this.resetTime = resetTime;
    }

    public UserItem toEntity() {
        return UserItem.builder()
            .restTime(LocalDateTime.parse(resetTime))
            .build();
    }
}
