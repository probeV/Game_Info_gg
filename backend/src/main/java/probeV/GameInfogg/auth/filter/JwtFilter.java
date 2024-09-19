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
    // Spring Security Filter Chain 보다 먼저 체크
    @Value("${user.active.url}")
    List<String> userActiveToken;

    @Value("${admin.active.url}")
    List<String> adminActiveToken;
    
    private final JwtTokenProvider jwtTokenProvider;

    // 실제 필터릴 로직
    // 토큰의 인증정보를 SecurityContext에 저장하는 역할 수행
    @Override
    protected void doFilterInternal(
        HttpServletRequest request, 
        HttpServletResponse response, 
        FilterChain filterChain
        ) throws IOException, ServletException {

        String requestURI = request.getRequestURI();

        log.info("현재 URI : " + requestURI);

        boolean isUserActive = userActiveToken.stream()
            .anyMatch(token -> token.endsWith("/*") && requestURI.startsWith(token.substring(0, token.length() - 2)));
        boolean isAdminActive = adminActiveToken.stream()
            .anyMatch(token -> token.endsWith("/*") && requestURI.startsWith(token.substring(0, token.length() - 2)));


        // 토큰이 필요없는 경우, 권한이 필요없는 요청 
        if(!isUserActive && !isAdminActive){
            filterChain.doFilter(request, response);
            return;
        }

        log.info("JWT 토큰이 필요한 URI : " + requestURI);
        
        String accessToken = request.getHeader("Authorization") != null ? request.getHeader("Authorization").substring(7) : null;

        // accessToken이 유효하다면
        if (accessToken != null && jwtTokenProvider.validateToken(accessToken)) {
            log.info("AccessToken이 유효합니다.");
            // 토큰에서 인증정보를 꺼내옴
            Authentication authentication = jwtTokenProvider.getAuthentication(accessToken);

            // 관리자 요청이지만 권한이 없을 경우, 에러 반환
            if (isAdminActive && !isAdminAuthenticated(authentication)) {
                log.info("관리자 권한이 없습니다.");
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "권한이 없습니다.");
                return;
            }

            // 꺼내온 인증정보를 SecurityContext에 저장
            SecurityContextHolder.getContext().setAuthentication(authentication);
            filterChain.doFilter(request, response);
        } 
        else {
            // 유효하지 않다면 RefreshToken을 확인
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if (cookie.getName().equals("RefreshToken")) {
                        String refreshToken = cookie.getValue();

                        // RefreshToken이 유효하다면, AccessToken 재발급 (자동 재로그인)
                        if (refreshToken != null && jwtTokenProvider.validateToken(refreshToken)) {
                            log.info("RefreshToken이 유효합니다.");
                            log.info("AccessToken 재발급 중...");

                            // 새로운 AccessToken 생성, 사용자 정보는 기존 accessToken에서 가져옴
                            Authentication authentication = jwtTokenProvider.getAuthentication(accessToken);
                            String newAccessToken = jwtTokenProvider.generateAccessToken(authentication).getAccessToken();
                            
                            // 새로운 AccessToken을 헤더에 추가
                            response.setHeader("Authorization", "Bearer " + newAccessToken);

                            // 관리자 요청이지만 권한이 없을 경우, 에러 반환
                            if (isAdminActive && !isAdminAuthenticated(authentication)) {
                                log.info("관리자 권한이 없습니다.");
                                response.sendError(HttpServletResponse.SC_FORBIDDEN, "권한이 없습니다.");
                                return;
                            }   

                            // 꺼내온 인증정보를 SecurityContext에 저장
                            SecurityContextHolder.getContext().setAuthentication(authentication);
                            filterChain.doFilter(request, response);
                        }
                        // RefreshToken이 유효하지 않다면 로그아웃
                        else {
                            log.info("RefreshToken이 유효하지 않습니다.");
                            log.info("재로그인 필요");
                            SecurityContextHolder.getContext().setAuthentication(null);

                            // 로그아웃 후 로그인 페이지로 리다이렉트
                            response.sendRedirect("/login");
                            return;
                        }
                    }
                }
            }


            log.info("AccessToken, RefreshToken이 없습니다.");
            log.info("로그인 페이지로 리다이렉트");
            SecurityContextHolder.getContext().setAuthentication(null);
            response.sendRedirect("/login");
            return;
        }
    }

    private boolean isAdminAuthenticated(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));
    }
}
