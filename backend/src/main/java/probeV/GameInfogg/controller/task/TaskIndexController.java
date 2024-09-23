package probeV.GameInfogg.controller.task;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
public class TaskIndexController {

    @GetMapping("/task")
    public String TaskListPage() {
        return "pages/tasklist/default_tasklist";
    }

}
