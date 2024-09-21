package probeV.GameInfogg.service.task;

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

    //UserTask 생성, 수정, 삭제
    void saveTasks(List<UserTaskListSaveorUpdateRequestDto> requestDto);
    void deleteTasks(List<UserTaskListDeleteRequestDto> requestDto);
} 