package probeV.GameInfogg.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@Controller
@RequiredArgsConstructor
public class MainController {

    @GetMapping("/")
    public String redirectToTaskListPage() {
        return "pages/main";
    }

    @GetMapping("/ads.txt")
    @ResponseBody
    public ResponseEntity<Resource> getAdsTxt() {
        Resource resource = new ClassPathResource("static/ads.txt");
        return ResponseEntity.ok().body(resource);
    }
}
