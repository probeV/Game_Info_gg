package probeV.GameInfogg.service.task;

import probeV.GameInfogg.controller.task.dto.response.DefaultTaskListResponseDto;

import java.util.List;

public interface DefaultTaskService {

    // TaskList Filter 조회
    List<DefaultTaskListResponseDto> getAllTaskList();
    List<DefaultTaskListResponseDto> getFilteredByModeTaskList(String mode);
    List<DefaultTaskListResponseDto> getFilteredByEventTaskList(String event);
    List<DefaultTaskListResponseDto> getFilteredByModeEventTaskList(String mode, String event);

}
