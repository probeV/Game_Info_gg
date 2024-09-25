package probeV.GameInfogg.controller.user.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import lombok.Builder;

@Getter @Setter
@NoArgsConstructor
public class UserItemListDeleteRequestDto {
    private Long id;

    @Builder
    public UserItemListDeleteRequestDto(Long id) {
        this.id = id;
    }
}
