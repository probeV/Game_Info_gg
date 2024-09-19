package probeV.GameInfogg.service.admin;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import probeV.GameInfogg.controller.admin.dto.request.DefaultTaskListSaveRequestDto;
import probeV.GameInfogg.controller.admin.dto.response.UserListResponseDto;


@Service
public interface AdminService {

    // 기본 숙제 체크 리스트 항목 (생성, 수정, 삭제
    public void saveTasks(List<DefaultTaskListSaveRequestDto> requestDto);

    // 유저 목록 조회
    public List<UserListResponseDto> getUserList(Pageable pageable);

}
