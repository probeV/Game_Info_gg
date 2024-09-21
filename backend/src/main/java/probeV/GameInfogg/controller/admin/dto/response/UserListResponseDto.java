package probeV.GameInfogg.controller.admin.dto.response;

import java.time.LocalDateTime;

import lombok.Builder;
import probeV.GameInfogg.domain.user.constant.RoleType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import probeV.GameInfogg.domain.user.User;

@Getter
@NoArgsConstructor
public class UserListResponseDto {
    private Long id;
    private String email;
    private String name;
    private RoleType roleType;
    private String provider;
    private LocalDateTime createdDate;

    @Builder
    public UserListResponseDto(Long id, String email, String name, RoleType roleType, String provider, LocalDateTime createdDate) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.roleType = roleType;
        this.provider = provider;
        this.createdDate = createdDate;
    }
    public UserListResponseDto(User entity) {
        this.id = entity.getId();
        this.email = entity.getEmail();
        this.name = entity.getName();
        this.roleType = entity.getRoleType();
        this.provider = entity.getProvider();
        this.createdDate = entity.getCreatedDate();
    }
}
