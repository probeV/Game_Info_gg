package probeV.GameInfogg.service.task;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import probeV.GameInfogg.controller.task.dto.response.DefaultTaskListResponseDto;
import probeV.GameInfogg.domain.task.constant.EventType;
import probeV.GameInfogg.domain.task.constant.ModeType;
import probeV.GameInfogg.repository.task.DefaultTaskRepository;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class DefaultTaskServiceImpl implements DefaultTaskService{

    private final DefaultTaskRepository defaultTaskRepository;

    // Task 전체 조회
    @Override
    public List<DefaultTaskListResponseDto> getAllTaskList() {
        log.debug("getAllTaskLIst");

        return defaultTaskRepository.findAll().stream()
                .map(DefaultTaskListResponseDto::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<DefaultTaskListResponseDto> getFilteredByModeTaskList(String mode) {
        log.debug("getFilteredByModeTaskList");

        ModeType modeType = ModeType.fromString(mode);

        return defaultTaskRepository.findByModeType(modeType).stream()
                .map(DefaultTaskListResponseDto::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<DefaultTaskListResponseDto> getFilteredByEventTaskList(String event) {
        log.debug("getFilteredByEventTaskList");

        EventType eventType = EventType.fromString(event);

        return defaultTaskRepository.findByEventType(eventType).stream()
                .map(DefaultTaskListResponseDto::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<DefaultTaskListResponseDto> getFilteredByModeEventTaskList(String mode, String event) {
        log.debug("getFilteredByModeEventTaskList");

        ModeType modeType = ModeType.fromString(mode);
        EventType eventType = EventType.fromString(event);

        return defaultTaskRepository.findByModeTypeAndEventType(modeType, eventType).stream()
                .map(DefaultTaskListResponseDto::new)
                .collect(Collectors.toList());
    }
}

