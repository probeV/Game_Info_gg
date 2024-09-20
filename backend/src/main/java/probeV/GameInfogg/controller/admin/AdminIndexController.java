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

    @GetMapping("/admin")
    public String AdminPage(){
        return "redirect:/admin/main";
    }

    @GetMapping("/admin/main")
    public String AdminMainPage(){
        return "pages/AdminPages/AdminMain";
    }

    @GetMapping("/admin/user")
    public String AdminUserPage(){
        return "pages/AdminPages/AdminUser";
    }

    @GetMapping("/admin/task")
    public String AdminTaskPage(){
        return "pages/AdminPages/AdminTask";
    }

    @GetMapping("/admin/taskUsage")
    public String AdminTaskUsagePage(){
        return "pages/AdminPages/AdminTaskUsage";
    }

    @GetMapping("/admin/taskSetting")
    public String AdminTaskSettingPage(){
        return "pages/AdminPages/AdminTaskSetting";
    }
}
