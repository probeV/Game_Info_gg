package probeV.GameInfogg.controller.task;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import probeV.GameInfogg.controller.task.dto.response.TaskListResponseDto;
import probeV.GameInfogg.service.tast.TaskService;

import java.util.List;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1")
public class TaskController {

    private final TaskService taskService;

    @GetMapping("/oncehumans/tasks")
    public List<TaskListResponseDto> taskList(){
        return taskService.viewTaskList();
    }


}
