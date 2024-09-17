package probeV.GameInfogg.auth.handler;

import java.nio.charset.StandardCharsets;
import java.sql.DriverManager;

import jakarta.servlet.http.Cookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.web.util.UriComponentsBuilder;

import com.nimbusds.oauth2.sdk.token.RefreshToken;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import probeV.GameInfogg.auth.dto.response.AccessTokenResponseDto;
import probeV.GameInfogg.auth.dto.response.RefreshTokenResponseDto;
import probeV.GameInfogg.auth.jwt.JwtTokenProvider;

@RequiredArgsConstructor
public class MyAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {

        DefaultOAuth2User defaultOAuth2User = (DefaultOAuth2User) authentication.getPrincipal();


        AccessTokenResponseDto accessTokenResponseDto = jwtTokenProvider.generateAccessToken(authentication);
        RefreshTokenResponseDto refreshTokenResponseDto = jwtTokenProvider.generateRefreshToken();

        // AccessToken을 HTTP Header에 담아 전송
        response.setHeader("Authorization", "Bearer " + accessTokenResponseDto.getAccessToken());

        // RefreshToken을 Cookie에 저장
        Cookie refreshTokenCookie = new Cookie("RefreshToken", refreshTokenResponseDto.getRefreshToken());
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setPath("/");
        response.addCookie(refreshTokenCookie);


    }
    
}
