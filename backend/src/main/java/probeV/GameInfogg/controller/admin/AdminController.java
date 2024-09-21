package probeV.GameInfogg.controller.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;

import probeV.GameInfogg.controller.admin.dto.request.DefaultTaskListDeleteDto;
import probeV.GameInfogg.controller.admin.dto.request.DefaultTaskListSaveorUpdateRequestDto;
import probeV.GameInfogg.service.admin.AdminService;
import probeV.GameInfogg.controller.admin.dto.response.UserPageResponseDto;

import java.util.List;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class AdminController {
    private final AdminService adminService;

    // 기본 숙제 체크 리스트 항목 설정 (생성, 수정)
    @PostMapping("/admins/tasks")
    public void saveOrUpdateTasks(
        @Valid @RequestBody List<DefaultTaskListSaveorUpdateRequestDto> requestDto
    ){
        adminService.saveTasks(requestDto);

        return;
    }

    // 기본 숙제 체크 리스트 항목 삭제
    @DeleteMapping("/admins/tasks")
    public void deleteTasks(
        @Valid @RequestBody List<DefaultTaskListDeleteDto> requestDto
    ){
        adminService.deleteTasks(requestDto);

        return;
    }

    // 유저 목록 조회
    @GetMapping("/admins/users")
    public UserPageResponseDto getUserList(@RequestParam(value="page", defaultValue="0") int page) {
        UserPageResponseDto userPage = adminService.getUserList(page);

        return userPage;
    }


}
