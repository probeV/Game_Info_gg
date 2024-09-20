package probeV.GameInfogg.controller.admin;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.stereotype.Controller;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
public class AdminIndexController {
    // 관리자 페이지    
    // 관리자 페이지 메인 페이지
    @GetMapping("/admin")
    public String AdminPage(){
        return "redirect:/admin/main";
    }

    // 관리자 페이지 메인 페이지
    @GetMapping("/admin/main")
    public String AdminMainPage(){
        return "pages/AdminPages/AdminMain";
    }

    // 관리자 페이지 유저 목록 페이지
    @GetMapping("/admin/user")
    public String AdminUserPage(){
        return "pages/AdminPages/AdminUserList";
    }

    // 관리자 페이지 기본 숙제 체크 리스트 설정 페이지
    @GetMapping("/admin/task")
    public String AdminTaskPage(){
        return "pages/AdminPages/AdminDefaultTaskListSetting";
    }

    // 관리자 페이지 숙제 체크 리스트 사용법 페이지
    @GetMapping("/admin/taskUsage")
    public String AdminTaskUsagePage(){
        return "pages/AdminPages/AdminTaskUsage";
    }

}
