package probeV.GameInfogg.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
public class MainController {

    @GetMapping("/")
    public String redirectToTaskListPage() {
        return "redirect:/task";
    }

    @GetMapping("/task")
    public String TaskListPage() {
        return "pages/TaskListPages/DefaultTaskList";
    }

    @GetMapping("/login")
    public String LoginPage(){
        return "pages/LoginPages/Login";
    }

    // 관리자 페이지    
    @GetMapping("/admin/main")
    public String AdminPage(){
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
