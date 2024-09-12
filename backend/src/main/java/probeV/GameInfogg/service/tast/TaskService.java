package probeV.GameInfogg.service.tast;

import probeV.GameInfogg.controller.task.dto.response.TaskListResponseDto;

import java.util.List;

public interface TaskService {

    // Task 전체 조회
    List<TaskListResponseDto> viewTaskList();
}
