package probeV.GameInfogg.auth.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import probeV.GameInfogg.auth.jwt.JwtTokenProvider;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;

import java.io.IOException;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    
    @Value("${jwt.active.url}")
    List<String> activeToken;
    
    private final JwtTokenProvider jwtTokenProvider;

    // 실제 필터릴 로직
    // 토큰의 인증정보를 SecurityContext에 저장하는 역할 수행
    @Override
    protected void doFilterInternal(
        HttpServletRequest request, 
        HttpServletResponse response, 
        FilterChain filterChain
        ) throws IOException, ServletException {
        
        // 토큰이 필요없는 경우, 권한이 필요없는 요청 
        if(!activeToken.contains(request.getRequestURI())){
            filterChain.doFilter(request, response);
            return;
        }

        log.info("JWT 토큰이 필요한 URI : " + request.getRequestURI());
        
        String accessToken = request.getHeader("Authorization").substring(7);

        // accessToken이 유효하다면
        if (accessToken != null && jwtTokenProvider.validateToken(accessToken)) {
            log.info("AccessToken이 유효합니다.");
            // 토큰에서 인증정보를 꺼내옴
            Authentication authentication = jwtTokenProvider.getAuthentication(accessToken);
            // 꺼내온 인증정보를 SecurityContext에 저장
            SecurityContextHolder.getContext().setAuthentication(authentication);
            // 필터 종료
            return;
        } 
        else {
            // 유효하지 않다면 RefreshToken을 확인
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if (cookie.getName().equals("RefreshToken")) {
                        String refreshToken = cookie.getValue();

                        // RefreshToken이 유효하다면
                        if (refreshToken != null && jwtTokenProvider.validateToken(refreshToken)) {
                            log.info("RefreshToken이 유효합니다.");
                            log.info("AccessToken 재발급 중...");

                            // 새로운 AccessToken 생성, 사용자 정보는 기존 accessToken에서 가져옴
                            Authentication authentication = jwtTokenProvider.getAuthentication(accessToken);
                            String newAccessToken = jwtTokenProvider.generateAccessToken(authentication).getAccessToken();
                            
                            // 새로운 AccessToken을 헤더에 추가
                            response.setHeader("Authorization", "Bearer " + newAccessToken);
                            // SecurityContext에 새로운 인증정보 저장
                            SecurityContextHolder.getContext().setAuthentication(authentication);
                        }
                        // RefreshToken이 유효하지 않다면 로그아웃
                        else {
                            log.info("RefreshToken이 유효하지 않습니다.");
                            log.info("재로그인 필요");
                            SecurityContextHolder.getContext().setAuthentication(null);
                        }
                    }
                }
            }
        }
        
    }

}
