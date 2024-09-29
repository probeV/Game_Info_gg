package probeV.GameInfogg.controller.user.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;


@Getter @Setter
@NoArgsConstructor
public class UserItemListDeleteRequestDto {
    @NotNull
    private Long userItemId;

    @Builder
    public UserItemListDeleteRequestDto(Long userItemId) {
        this.userItemId = userItemId;
    }
}
