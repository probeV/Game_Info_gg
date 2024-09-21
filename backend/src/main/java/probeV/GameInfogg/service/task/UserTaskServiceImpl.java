package probeV.GameInfogg.service.task;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import probeV.GameInfogg.auth.SecurityUtil;
import probeV.GameInfogg.controller.user.dto.request.UserTaskListDeleteRequestDto;
import probeV.GameInfogg.controller.user.dto.request.UserTaskListSaveorUpdateRequestDto;
import probeV.GameInfogg.controller.user.dto.response.UserTaskListResponseDto;
import probeV.GameInfogg.domain.task.constant.EventType;
import probeV.GameInfogg.domain.task.constant.ModeType;
import probeV.GameInfogg.repository.task.UserTaskRepository;
import org.springframework.transaction.annotation.Transactional;
import probeV.GameInfogg.domain.user.User;
import probeV.GameInfogg.exception.task.TaskNotFoundException;
import probeV.GameInfogg.repository.user.UserRepository;
import probeV.GameInfogg.domain.task.UserTask;
import java.util.Set;
import java.util.Map;
import java.time.DayOfWeek;
import java.time.LocalTime;


@Slf4j
@Service
@RequiredArgsConstructor
public class UserTaskServiceImpl implements UserTaskService {

    private final UserTaskRepository userTaskRepository;
    private final UserRepository userRepository;

    @Override
    public List<UserTaskListResponseDto> getAllUserTaskList() {
        // 모든 사용자 작업 목록을 가져오는 로직을 구현합니다.
        log.info("UserTaskService : getAllTaskList");

        // 유저 찾기
        String attributeCode = SecurityUtil.getAttributeCode();
        User user = userRepository.findByAttributeCode(attributeCode)
            .orElseThrow(() -> new IllegalArgumentException("User not found"));
        Long userId = user.getId();

        return userTaskRepository.findByUserId(userId).stream()
                .map(UserTaskListResponseDto::new)
                .collect(Collectors.toList());
    }


    @Override
    public List<UserTaskListResponseDto> getFilteredByModeUserTaskList(String mode) {
        // 모드에 따라 사용자 작업 목록을 필터링하는 로직을 구현합니다.
        log.info("UserTaskService : getFilteredByModeTaskList");

        // 유저 찾기
        String attributeCode = SecurityUtil.getAttributeCode();
        User user = userRepository.findByAttributeCode(attributeCode)
            .orElseThrow(() -> new IllegalArgumentException("User not found"));
        Long userId = user.getId();

        return userTaskRepository.findByUserIdAndModeType(userId, ModeType.fromString(mode)).stream()
                .map(UserTaskListResponseDto::new)
                .collect(Collectors.toList());

    }


    @Override
    public List<UserTaskListResponseDto> getFilteredByEventUserTaskList(String event) {
        // 이벤트에 따라 사용자 작업 목록을 필터링하는 로직을 구현합니다.
        log.info("UserTaskService : getFilteredByEventTaskList");

        // 유저 찾기
        String attributeCode = SecurityUtil.getAttributeCode();
        User user = userRepository.findByAttributeCode(attributeCode)
            .orElseThrow(() -> new IllegalArgumentException("User not found"));
        Long userId = user.getId();

        return userTaskRepository.findByUserIdAndEventType(userId, EventType.fromString(event)).stream()
                .map(UserTaskListResponseDto::new)
                .collect(Collectors.toList());

    }


    @Override
    public List<UserTaskListResponseDto> getFilteredByModeEventUserTaskList(String mode, String event) {
        // 모드와 이벤트에 따라 사용자 작업 목록을 필터링하는 로직을 구현합니다.
        log.info("UserTaskService : getFilteredByModeEventTaskList");

        // 유저 찾기
        String attributeCode = SecurityUtil.getAttributeCode();
        User user = userRepository.findByAttributeCode(attributeCode)
            .orElseThrow(() -> new IllegalArgumentException("User not found"));
        Long userId = user.getId();

        return userTaskRepository.findByUserIdAndModeTypeAndEventType(userId, ModeType.fromString(mode), EventType.fromString(event)).stream()
                .map(UserTaskListResponseDto::new)
                .collect(Collectors.toList());

    }


    @Override
    @Transactional
    public void saveTasks(List<UserTaskListSaveorUpdateRequestDto> requestDto) {
        // 사용자 작업 목록을 생성하는 로직을 구현합니다.
        log.info("UserTaskService : saveTasks");

        // 유저 찾기
        String attributeCode = SecurityUtil.getAttributeCode();
        User user = userRepository.findByAttributeCode(attributeCode)
            .orElseThrow(() -> new IllegalArgumentException("User not found"));
        Long userId = user.getId();

        // 존재하는 UserTask 목록 가져오기
        List<UserTask> existingUserTasks = userTaskRepository.findByUserId(userId);

        // 존재하는 UserTask ID를 Key로, UserTask를 Value로 하는 Map 생성
        Map<Long, UserTask> userTaskMap = existingUserTasks.stream()
            .collect(Collectors.toMap(UserTask::getId, Function.identity()));

        // 수정 및 생성 처리
        for (UserTaskListSaveorUpdateRequestDto dto : requestDto) {
            Long id = dto.getId();

            // 이미 존재하는 경우
            if (id != null && userTaskMap.containsKey(id)) {
                // 수정 로직 호출
                log.info("saveTasks 수정 로직 호출" + id);

                UserTask userTask = userTaskMap.get(id);
                userTask.update(dto.getName(), 
                        dto.getResetDayOfWeek() != null ? DayOfWeek.valueOf(dto.getResetDayOfWeek()) : null, 
                        dto.getResetTime() != null ? LocalTime.parse(dto.getResetTime()) : null, 
                        dto.getSortPriority(), 
                        EventType.fromString(dto.getEvent()));
            } 
            // 존재하지 않는 경우
            else if (id == null) {
                // 생성 로직
                log.info("saveTasks 생성 로직 호출" + id);

                UserTask userTask = dto.toEntity();
                userTask.setUser(user);
                userTaskRepository.save(userTask);
            }
            else {
                log.error("saveTasks 오류 발생" + id);

                throw new TaskNotFoundException("Task not found" + id);
            }
        }

    }
    

    @Override
    @Transactional
    public void deleteTasks(List<UserTaskListDeleteRequestDto> requestDto) {
        // 사용자 작업 목록을 삭제하는 로직을 구현합니다.
        log.info("UserTaskService : deleteTasks");

        // 유저 찾기
        String attributeCode = SecurityUtil.getAttributeCode();
        User user = userRepository.findByAttributeCode(attributeCode)
            .orElseThrow(() -> new IllegalArgumentException("User not found"));
        Long userId = user.getId();

        // 존재하는 UserTask 목록 가져오기
        List<UserTask> existingUserTasks = userTaskRepository.findByUserId(userId);

        // 존재하는 UserTask ID 목록 가져오기
        Set<Long> existingUserTaskIds = existingUserTasks.stream()
            .map(UserTask::getId)
            .collect(Collectors.toSet());

        // 존재하는 UserTask ID를 Key로, UserTask를 Value로 하는 Map 생성
        Map<Long, UserTask> userTaskMap = existingUserTasks.stream()
            .collect(Collectors.toMap(UserTask::getId, Function.identity()));


        for (UserTaskListDeleteRequestDto dto : requestDto) {
            if(userTaskMap.get(dto.getId()) != null) {
                existingUserTaskIds.remove(dto.getId());
            }
        }

        for(Long taskId : existingUserTaskIds) {
            log.info("deleteTasks 삭제 로직 호출");
            if(userTaskMap.get(taskId) != null) {

                userTaskRepository.deleteById(taskId);
            }
        }
    }
}