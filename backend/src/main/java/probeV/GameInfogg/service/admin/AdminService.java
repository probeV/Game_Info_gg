package probeV.GameInfogg.service.admin;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import probeV.GameInfogg.controller.admin.dto.request.DefaultTaskListSaveRequestDto;
import probeV.GameInfogg.domain.user.User;


@Service
public interface AdminService {

    // 기본 숙제 체크 리스트 항목 (생성, 수정, 삭제
    public void saveTasks(List<DefaultTaskListSaveRequestDto> requestDto);

    // 유저 목록 조회
    public Page<User> getUserList(int page);

}
