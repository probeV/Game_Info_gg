package probeV.GameInfogg.service.tast;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import probeV.GameInfogg.controller.task.dto.response.TaskListResponseDto;
import probeV.GameInfogg.domain.task.Task;
import probeV.GameInfogg.repository.TaskRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService{

    private final TaskRepository taskRepository;

    // Task 전체 조회
    @Override
    public List<TaskListResponseDto> viewTaskList() {
        List<Task> tasks = taskRepository.findAll();

        List<TaskListResponseDto> responseDto = new ArrayList<>();

        for(Task task : tasks){
            TaskListResponseDto dto = new TaskListResponseDto(task);
            responseDto.add(dto);
        }

        return responseDto;
    }
}

