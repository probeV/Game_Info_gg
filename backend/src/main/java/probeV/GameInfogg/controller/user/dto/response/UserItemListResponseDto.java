package probeV.GameInfogg.controller.user.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import probeV.GameInfogg.domain.user.UserItem;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class UserItemListResponseDto {
    private Long userItemId;
    private Long itemId;
    private String name;
    private String imageUrl;
    private LocalDateTime resetTime;

    public UserItemListResponseDto(UserItem userItem){
        this.userItemId = userItem.getId();
        this.itemId = userItem.getItem().getId();
        this.name = userItem.getItem().getName();
        this.imageUrl = userItem.getItem().getImageUrl();
        this.resetTime = userItem.getRestTime();
    }
}
