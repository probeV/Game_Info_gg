package probeV.GameInfogg.controller.help;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
public class HelpIndexController {

    @GetMapping("/help")
    public String help() {
        return "pages/HelpPages/help";
    }
}

