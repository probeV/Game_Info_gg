package probeV.GameInfogg.service.admin;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import probeV.GameInfogg.domain.task.DefaultTask;
import probeV.GameInfogg.repository.task.DefaultTaskRepository;
import probeV.GameInfogg.repository.user.UserRepository;
import probeV.GameInfogg.domain.task.constant.EventType;
import probeV.GameInfogg.domain.task.constant.FrequencyType;
import probeV.GameInfogg.domain.task.constant.ModeType;
import probeV.GameInfogg.exception.task.TaskNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import probeV.GameInfogg.domain.user.User;
import probeV.GameInfogg.controller.admin.dto.response.UserListResponseDto;
import probeV.GameInfogg.controller.admin.dto.request.DefaultTaskSaveRequestDto;
import probeV.GameInfogg.controller.admin.dto.request.DefaultTaskUpdateDto;


@Slf4j
@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final DefaultTaskRepository defaultTaskRepository;
    private final UserRepository userRepository;

    // 기본 숙제 체크 리스트 항목 생성
    @Override
    public void createTask(String mode, String frequency, String event, DefaultTaskSaveRequestDto requestDto) {

        ModeType modeType = ModeType.valueOf(mode);
        FrequencyType frequencyType = FrequencyType.valueOf(frequency);
        EventType eventType = EventType.valueOf(event);


        DefaultTask entity = DefaultTask.builder()
            .name(requestDto.getName())
            .modeType(modeType)
            .frequencyType(frequencyType)
            .eventType(eventType)
            .dayOfWeek(requestDto.getDayOfWeek())
            .time(requestDto.getTime())
            .build();

        defaultTaskRepository.save(entity);
    }

    // 기본 숙제 체크 리스트 항목 수정
    @Override
    public void updateTask(Integer id, String mode, String frequency, String event, DefaultTaskUpdateDto requestDto) {
        DefaultTask entity = defaultTaskRepository.findById(id)
            .orElseThrow(() -> new TaskNotFoundException("Task not found with id: " + id));

        ModeType modeType = ModeType.valueOf(mode);
        FrequencyType frequencyType = FrequencyType.valueOf(frequency);
        EventType eventType = EventType.valueOf(event);

        entity.update(requestDto.getName(), modeType, frequencyType, eventType, requestDto.getDayOfWeek(), requestDto.getTime() );    
    }

    // 기본 숙제 체크 리스트 항목 삭제
    @Override
    public void deleteTask(Integer id) {
        DefaultTask entity = defaultTaskRepository.findById(id)
            .orElseThrow(() -> new TaskNotFoundException("Task not found with id: " + id));

        defaultTaskRepository.delete(entity);
    }

    // 유저 목록 조회
    @Override
    public List<UserListResponseDto> getUserList(Pageable pageable) {
        Page<User> entities = userRepository.findAll(pageable);

        return entities.stream().map(UserListResponseDto::new).toList();
    }
}
