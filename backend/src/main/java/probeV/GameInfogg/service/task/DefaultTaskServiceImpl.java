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

    @Override
    public List<DefaultTaskListResponseDto> getAllTaskList() {
        // 기본 체크리스트를 조회하는 로직을 구현합니다.
        log.info("getAllTaskList 기본 체크리스트 조회");

        return defaultTaskRepository.findAll().stream()
                .map(DefaultTaskListResponseDto::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<DefaultTaskListResponseDto> getFilteredByModeTaskList(String mode) {
        // 모드에 따라 기본 체크리스트를 조회하는 로직을 구현합니다.
        log.info("getFilteredByModeTaskList 모드에 따라 기본 체크리스트 조회");

        ModeType modeType = ModeType.fromString(mode);

        return defaultTaskRepository.findByModeType(modeType).stream()
                .map(DefaultTaskListResponseDto::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<DefaultTaskListResponseDto> getFilteredByEventTaskList(String event) {
        // 이벤트에 따라 기본 체크리스트를 조회하는 로직을 구현합니다.
        log.info("getFilteredByEventTaskList 이벤트에 따라 기본 체크리스트 조회");

        EventType eventType = EventType.fromString(event);

        return defaultTaskRepository.findByEventType(eventType).stream()
                .map(DefaultTaskListResponseDto::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<DefaultTaskListResponseDto> getFilteredByModeEventTaskList(String mode, String event) {
        // 모드와 이벤트에 따라 기본 체크리스트를 조회하는 로직을 구현합니다.
        log.info("getFilteredByModeEventTaskList 모드와 이벤트에 따라 기본 체크리스트 조회");

        ModeType modeType = ModeType.fromString(mode);
        EventType eventType = EventType.fromString(event);

        return defaultTaskRepository.findByModeTypeAndEventType(modeType, eventType).stream()
                .map(DefaultTaskListResponseDto::new)
                .collect(Collectors.toList());
    }
}

