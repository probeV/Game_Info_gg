package probeV.GameInfogg.controller.admin.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import probeV.GameInfogg.domain.user.User;
import java.util.List;

@Getter
@NoArgsConstructor
public class UserPageResponseDto {
    private List<UserListResponseDto> userList;
    private int totalPages;
    private long totalElements;
    private int number;
    private int size;

    public UserPageResponseDto(Page<User> page) {
        this.userList = page.map(UserListResponseDto::new).getContent();
        this.totalPages = page.getTotalPages();
        this.totalElements = page.getTotalElements();
        this.number = page.getNumber();
        this.size = page.getSize();
    }
}