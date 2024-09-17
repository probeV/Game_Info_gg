package probeV.GameInfogg.service.task;

import probeV.GameInfogg.controller.task.dto.response.TaskListResponseDto;

import java.util.List;

public interface TaskService {

    // TaskList Filter 조회
    List<TaskListResponseDto> getAllTaskList();
    List<TaskListResponseDto> getFilteredByModeTaskList(String mode);
    List<TaskListResponseDto> getFilteredByEventTaskList(String event);

    List<TaskListResponseDto> getFilteredByModeEventTaskList(String mode, String event);

}
