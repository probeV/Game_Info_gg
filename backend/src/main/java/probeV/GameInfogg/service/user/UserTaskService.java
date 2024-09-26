package probeV.GameInfogg.service.user;

import java.util.List;

import probeV.GameInfogg.controller.user.dto.request.UserTaskListDeleteRequestDto;
import probeV.GameInfogg.controller.user.dto.request.UserTaskListSaveorUpdateRequestDto;
import probeV.GameInfogg.controller.user.dto.response.UserTaskListResponseDto;


public interface UserTaskService {
    //UserTask Filter 조회
    List<UserTaskListResponseDto> getAllUserTaskList();
    List<UserTaskListResponseDto> getFilteredByModeUserTaskList(String mode);
    List<UserTaskListResponseDto> getFilteredByEventUserTaskList(String event);
    List<UserTaskListResponseDto> getFilteredByModeEventUserTaskList(String mode, String event);

    //UserTask 생성, 수정
    void saveTasks(List<UserTaskListSaveorUpdateRequestDto> requestDto);
    //UserTask 삭제 (UserTaskListDeleteRequestDto에는 현재 남아있는 Task의 ID만 담아서 보냄 즉, dto에 없는 애들만 삭제)
    void deleteTasks(List<UserTaskListDeleteRequestDto> requestDto);
} 