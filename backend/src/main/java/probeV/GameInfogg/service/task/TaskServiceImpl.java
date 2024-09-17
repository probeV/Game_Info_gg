package probeV.GameInfogg.service.task;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import probeV.GameInfogg.controller.task.dto.response.TaskListResponseDto;
import probeV.GameInfogg.domain.task.constant.EventType;
import probeV.GameInfogg.domain.task.constant.ModeType;
import probeV.GameInfogg.repository.task.TaskRepository;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService{

    private final TaskRepository taskRepository;

    // Task 전체 조회
    @Override
    public List<TaskListResponseDto> getAllTaskList() {
        log.debug("getAllTaskLIst");

        return taskRepository.findAll().stream()
                .map(TaskListResponseDto::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<TaskListResponseDto> getFilteredByModeTaskList(String mode) {
        log.debug("getFilteredByModeTaskList");

        ModeType modeType = ModeType.valueOf(mode.toUpperCase());

        return taskRepository.findByModeType(modeType).stream()
                .map(TaskListResponseDto::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<TaskListResponseDto> getFilteredByEventTaskList(String event) {
        log.debug("getFilteredByEventTaskList");

        EventType eventType = EventType.valueOf(event.toUpperCase());

        return taskRepository.findByEventType(eventType).stream()
                .map(TaskListResponseDto::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<TaskListResponseDto> getFilteredByModeEventTaskList(String mode, String event) {
        log.debug("getFilteredByModeEventTaskList");

        ModeType modeType = ModeType.valueOf(mode.toUpperCase());
        EventType eventType = EventType.valueOf(event.toUpperCase());

        return taskRepository.findByModeTypeAndEventType(modeType, eventType).stream()
                .map(TaskListResponseDto::new)
                .collect(Collectors.toList());
    }
}

