package probeV.GameInfogg.controller.auth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class AuthController {

    @GetMapping("/login")
    public String ToLoginPage(){
        return "Login/Login";
    }

}
