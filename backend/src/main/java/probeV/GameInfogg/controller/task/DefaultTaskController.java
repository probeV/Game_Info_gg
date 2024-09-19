package probeV.GameInfogg.controller.task;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import probeV.GameInfogg.controller.task.dto.response.DefaultTaskListResponseDto;
import probeV.GameInfogg.service.task.DefaultTaskService;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("api/v1")
public class DefaultTaskController {

    private final DefaultTaskService defaultTaskService;

    // AJAX 요청을 처리하는 컨트롤러
    @GetMapping("/tasks/filter")
    @ResponseBody
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
}
