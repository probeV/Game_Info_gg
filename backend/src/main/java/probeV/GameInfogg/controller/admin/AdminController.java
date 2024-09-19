package probeV.GameInfogg.controller.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;
import probeV.GameInfogg.controller.admin.dto.request.DefaultTaskSaveRequestDto;
import probeV.GameInfogg.service.admin.AdminService;
import probeV.GameInfogg.controller.admin.dto.request.DefaultTaskUpdateDto;
import probeV.GameInfogg.controller.admin.dto.response.UserListResponseDto;
import java.util.List;




@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class AdminController {
    private final AdminService adminService;

    // 기본 숙제 체크 리스트 항목 생성
    @PostMapping("/admins/tasks")
    public void createTask(
        @RequestParam("mode") String mode,
        @RequestParam("frequency") String frequency,
        @RequestParam("event") String event,
        @RequestBody DefaultTaskSaveRequestDto requestDto
    ){
        adminService.createTask(mode, frequency, event, requestDto);
    }

    // 기본 숙제 체크 리스트 항목 수정
    @PutMapping("/admins/tasks/{id}")
    public void updateTask(
        @PathVariable("id") Integer id,
        @RequestParam("mode") String mode,
        @RequestParam("frequency") String frequency,
        @RequestParam("event") String event,
        @RequestBody DefaultTaskUpdateDto requestDto
    ){
        adminService.updateTask(id, mode, frequency, event, requestDto);
    }

    // 기본 숙제 체크 리스트 항목 삭제
    @DeleteMapping("/admins/tasks/{id}")
    public void deleteTask(
        @PathVariable("id") Integer id
    ){
        adminService.deleteTask(id);
    }

    // 유저 목록 조회
    @GetMapping("/admins/users")
    public List<UserListResponseDto> getUserList(Pageable pageable){
        return adminService.getUserList(pageable);
    }


}
