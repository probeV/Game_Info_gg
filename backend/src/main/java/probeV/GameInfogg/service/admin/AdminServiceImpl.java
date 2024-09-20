package probeV.GameInfogg.service.admin;

import java.util.List;
import java.util.stream.Collectors;

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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.method.P;

import probeV.GameInfogg.domain.user.User;
import probeV.GameInfogg.controller.admin.dto.request.DefaultTaskListSaveRequestDto;
import probeV.GameInfogg.controller.admin.dto.response.UserListResponseDto;
import probeV.GameInfogg.controller.admin.dto.response.UserPageResponseDto;

import java.util.Set;
import java.util.Map;
import java.util.function.Function;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final DefaultTaskRepository defaultTaskRepository;
    private final UserRepository userRepository;

    // 기본 숙제 체크 리스트 항목 생성
    @Override
    @Transactional
    public void saveTasks(List<DefaultTaskListSaveRequestDto> requestDto) {
        log.info("AdminService : saveTasks");

        // DB에서 현재 존재하는 Task ID 목록 가져오기
        List<DefaultTask> existingTasks = defaultTaskRepository.findAll();

        // 존재하는 Task ID 목록 가져오기
        Set<Integer> existingTaskIds = existingTasks.stream()
            .map(DefaultTask::getId)
            .collect(Collectors.toSet());

        // 존재하는 Task ID를 Key로, Task를 Value로 하는 Map 생성
        Map<Integer, DefaultTask> taskMap = existingTasks.stream()
            .collect(Collectors.toMap(DefaultTask::getId, Function.identity()));

        // 수정 및 생성 처리
        for (DefaultTaskListSaveRequestDto dto : requestDto) {
            // 이미 존재하는 경우
            if (dto.getId() != null && taskMap.containsKey(dto.getId())) {
                // 수정 로직 호출
                log.info("saveTasks 수정 로직 호출" + dto.getId());

                DefaultTask task = taskMap.get(dto.getId());
                task.update(dto.getName(), ModeType.fromString(dto.getMode()), FrequencyType.fromString(dto.getFrequency()), EventType.fromString(dto.getEvent()));
                // existingTaskIds에서 제거
                existingTaskIds.remove(dto.getId());
            } 
            // 존재하지 않는 경우
            else if (dto.getId() == null) {
                // 생성 로직
                log.info("saveTasks 생성 로직 호출" + dto.getId());

                defaultTaskRepository.save(dto.toEntity());
            }
            else {
                log.error("saveTasks 오류 발생" + dto.getId());

                throw new TaskNotFoundException("Task not found");
            }
        }

        // 삭제 처리
        for (Integer id : existingTaskIds) {
            // 삭제 로직 호출
            log.info("saveTasks 삭제 로직 호출" + id);

            defaultTaskRepository.deleteById(id);
        }

    }


    // 유저 목록 조회
    @Override
    public UserPageResponseDto getUserList(int page) {
        log.info("AdminService : getUserList");

        Pageable pageable = PageRequest.of(page, 10);
        Page<User> entities = userRepository.findAll(pageable);

        return new UserPageResponseDto(entities);
    }
}
