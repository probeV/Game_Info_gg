package probeV.GameInfogg.controller.user.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import probeV.GameInfogg.domain.user.UserItem;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class UserItemListResponseDto {
    private Long id;
    private String name;
    private String imageUrl;
    private LocalDateTime resetTime;

    public UserItemListResponseDto(UserItem userItem){
        this.id = userItem.getId();
        this.name = userItem.getItem().getName();
        this.imageUrl = userItem.getItem().getImageUrl();
        this.resetTime = userItem.getRestTime();
    }
}
