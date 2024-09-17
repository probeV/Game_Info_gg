package probeV.GameInfogg.auth.handler;

import jakarta.servlet.http.Cookie;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import probeV.GameInfogg.auth.dto.response.AccessTokenResponseDto;
import probeV.GameInfogg.auth.dto.response.RefreshTokenResponseDto;
import probeV.GameInfogg.auth.jwt.JwtTokenProvider;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class MyAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {

        DefaultOAuth2User defaultOAuth2User = (DefaultOAuth2User) authentication.getPrincipal();

        log.info("onAuthenticationSuccess token 생성 시작");

        AccessTokenResponseDto accessTokenResponseDto = jwtTokenProvider.generateAccessToken(authentication);
        RefreshTokenResponseDto refreshTokenResponseDto = jwtTokenProvider.generateRefreshToken();


        // RefreshToken을 Cookie에 저장
        Cookie refreshTokenCookie = new Cookie("RefreshToken", refreshTokenResponseDto.getRefreshToken());
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setPath("/");
        response.addCookie(refreshTokenCookie);

        // "/"로 리다이렉트
        // AccessToken을 HTTP Header에 담아 전송
        response.setHeader("Authorization", "Bearer " + accessTokenResponseDto.getAccessToken());

    }
    
}
