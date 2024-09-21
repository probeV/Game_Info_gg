package probeV.GameInfogg.controller.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@Slf4j
@Controller
@RequiredArgsConstructor
public class UserIndexController {

    @GetMapping("/user/task")
    public String getUserTask(){
        return "pages/TaskListPages/userTaskList";
    }
}


