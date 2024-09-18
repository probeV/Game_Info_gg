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
    public String redirectToTasks() {
        return "redirect:/tasks";
    }

    @GetMapping("/tasks")
    public String taskList() {
        return "pages/TaskListPages/DefaultTaskList";
    }

    @GetMapping("/logins")
    public String ToLoginPage(){
        return "pages/LoginPages/Login";
    }

}
