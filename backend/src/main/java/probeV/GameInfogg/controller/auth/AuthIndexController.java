package probeV.GameInfogg.controller.auth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

@Slf4j
@Controller
@RequiredArgsConstructor
public class AuthIndexController {

    @GetMapping("/login")
    public String LoginPage(){
        return "pages/login/login";
    }

    @GetMapping("/logout")
    public String logout(HttpServletResponse response) {
        // RefreshToken 쿠키 삭제
        Cookie cookie = new Cookie("RefreshToken", null);

        cookie.setMaxAge(0); // 쿠키 만료 시간 설정
        cookie.setPath("/"); // 쿠키 경로 설정

        response.addCookie(cookie); // 쿠키 추가
        return "redirect:/";
    }
}
