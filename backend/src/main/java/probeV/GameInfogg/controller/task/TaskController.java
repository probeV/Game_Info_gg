package probeV.GameInfogg.controller.task;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import probeV.GameInfogg.controller.task.dto.response.DefaultTaskListResponseDto;
import probeV.GameInfogg.service.task.DefaultTaskService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class TaskController {

    private final DefaultTaskService defaultTaskService;

    // Default Task Filter 조회
    @GetMapping("/tasks/filters")
    public List<DefaultTaskListResponseDto> filterTasks(
            @RequestParam("mode") String mode,
            @RequestParam("event") String event) {
        // 모드, 이벤트에 따른 필터링 로직 수행
        List<DefaultTaskListResponseDto> tasks;

        try {
            if ("ALL".equalsIgnoreCase(mode) && "ALL".equalsIgnoreCase(event)) {
                tasks = defaultTaskService.getAllTaskList();
            } else if ("ALL".equalsIgnoreCase(mode)) {
                tasks = defaultTaskService.getFilteredByEventTaskList(event);
            } else if ("ALL".equalsIgnoreCase(event)) {
                tasks = defaultTaskService.getFilteredByModeTaskList(mode);
            } else {
                tasks = defaultTaskService.getFilteredByModeEventTaskList(mode, event);
            }

            // JSON으로 필터링된 데이터를 반환
            return tasks;
        } catch (Exception e) {
            log.error("Error filtering tasks: {}", e.getMessage());
            return null; // 에러 발생 시 null 반환
        }
    }

    // User Task Filter 조회
//    @GetMapping("/users/tasks/filters/{id}")
//    public List<UserTaskListResponseDto> filterUserTasks(
//            @RequestParam("mode") String mode,
//            @RequestParam("event") String event){
//
//    )

}
