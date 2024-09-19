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

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import probeV.GameInfogg.controller.admin.dto.request.DefaultTaskListSaveRequestDto;
import probeV.GameInfogg.service.admin.AdminService;
import probeV.GameInfogg.controller.admin.dto.response.UserListResponseDto;
import java.util.List;




@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class AdminController {
    private final AdminService adminService;

    // 기본 숙제 체크 리스트 항목 설정 (생성, 수정, 삭제)
    @PostMapping("/admins/task")
    public void saveTasks(
        @Valid @RequestBody List<DefaultTaskListSaveRequestDto> requestDto
    ){
        adminService.saveTasks(requestDto);
    }


    // 유저 목록 조회
    @GetMapping("/admins/user")
    public List<UserListResponseDto> getUserList(Pageable pageable){
        return adminService.getUserList(pageable);
    }


}
