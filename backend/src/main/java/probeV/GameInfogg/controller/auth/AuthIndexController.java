package probeV.GameInfogg.controller.auth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
public class AuthIndexController {

    @GetMapping("/login")
    public String LoginPage(){
        return "pages/LoginPages/Login";
    }
}
