package probeV.GameInfogg.controller.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.http.ResponseEntity;

import probeV.GameInfogg.controller.user.dto.response.UserTaskListResponseDto;
import probeV.GameInfogg.controller.user.dto.request.UserTaskListSaveorUpdateRequestDto;
import probeV.GameInfogg.controller.user.dto.request.UserTaskListDeleteRequestDto;
import probeV.GameInfogg.service.task.UserTaskService;


import java.util.List;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class UserController {
    
    private final UserTaskService userTaskService;

    // User Task Filter 조회
    @GetMapping("/users/tasks/filters")
    public List<UserTaskListResponseDto> filterUserTasks(
            @RequestParam("mode") String mode,
            @RequestParam("event") String event
            ){
        // 모드, 이벤트에 따른 필터링 로직 수행
        List<UserTaskListResponseDto> tasks;

        try{
            if("ALL".equalsIgnoreCase(mode) && "ALL".equalsIgnoreCase(event)){
                tasks = userTaskService.getAllUserTaskList();
            }else if("ALL".equalsIgnoreCase(mode)){
                tasks = userTaskService.getFilteredByEventUserTaskList(event);
            }else if("ALL".equalsIgnoreCase(event)){
                tasks = userTaskService.getFilteredByModeUserTaskList(mode);
            }else{
                tasks = userTaskService.getFilteredByModeEventUserTaskList(mode, event);
            }

            return tasks;

        }catch(Exception e){

            log.error("Error filtering tasks: {}", e.getMessage());
            return null;
        }
    }

    // 나의 숙제 체크 리스트 항목 설정 (생성, 수정)
    @PostMapping("/users/tasks")
    public ResponseEntity<Void> createUserTask(@Valid @RequestBody List<UserTaskListSaveorUpdateRequestDto> requestDto){
        userTaskService.saveTasks(requestDto);
        return ResponseEntity.ok().build();
    }

    // 나의 숙제 체크 리스트 항목 삭제
    @DeleteMapping("/users/tasks")
    public ResponseEntity<Void> deleteUserTask(@RequestBody List<UserTaskListDeleteRequestDto> requestDto){
        userTaskService.deleteTasks(requestDto);
        return ResponseEntity.ok().build();
    }
}

