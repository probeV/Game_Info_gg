package probeV.GameInfogg.service.admin;

import java.util.List;

import org.springframework.stereotype.Service;

import probeV.GameInfogg.controller.admin.dto.request.DefaultTaskListDeleteDto;
import probeV.GameInfogg.controller.admin.dto.request.DefaultTaskListSaveorUpdateRequestDto;
import probeV.GameInfogg.controller.admin.dto.response.UserPageResponseDto;


@Service
public interface AdminService {

    // 기본 숙제 체크 리스트 항목 (생성, 수정, 삭제
    public void saveTasks(List<DefaultTaskListSaveorUpdateRequestDto> requestDto);

    // 기본 숙제 체크 리스트 항목 삭제
    public void deleteTasks(List<DefaultTaskListDeleteDto> requestDto);

    // 유저 목록 조회
    public UserPageResponseDto getUserList(int page);

}
