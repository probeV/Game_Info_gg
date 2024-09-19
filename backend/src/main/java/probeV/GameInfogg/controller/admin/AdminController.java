package probeV.GameInfogg.controller.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
import jakarta.validation.Valid;
import org.springframework.ui.Model; // 추가

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import probeV.GameInfogg.controller.admin.dto.request.DefaultTaskListSaveRequestDto;
import probeV.GameInfogg.service.admin.AdminService;
import probeV.GameInfogg.domain.user.User;
import org.springframework.data.domain.Page;

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
    public String getUserList(@RequestParam(value="page", defaultValue="0") int page, Model model) {
        Page<User> userList = adminService.getUserList(page);
        model.addAttribute("userList", userList);
        
        return "pages/AdminPages/AdminUser"; // 반환할 템플릿 이름
    }


}
