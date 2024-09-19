package probeV.GameInfogg.controller.admin.dto.response;


import lombok.Getter;
import lombok.NoArgsConstructor;
import probeV.GameInfogg.domain.user.User;
import probeV.GameInfogg.domain.user.constant.RoleType;


@Getter
@NoArgsConstructor
public class UserListResponseDto {
    private Long id;
    private String email;
    private String name;
    private RoleType roleType;
    private String provider;

    public UserListResponseDto(User entity) {
        this.id = entity.getId();
        this.email = entity.getEmail();
        this.name = entity.getName();
        this.roleType = entity.getRoleType();
        this.provider = entity.getProvider();
    }
}
