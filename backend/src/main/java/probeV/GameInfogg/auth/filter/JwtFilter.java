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
    @Value("${user.active.api}")
    List<String> userActiveToken;

    @Value("${admin.active.api}")
    List<String> adminActiveToken;

    @Value("${token.ignore.url}")
    List<String> ignoreUrl;
    
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

        boolean isIgnoreUrl = ignoreUrl.stream()
            .anyMatch(token -> token.endsWith("/*") && requestURI.startsWith(token.substring(0, token.length() - 2)));

        if(isIgnoreUrl){
            log.info("JwtFilter 무시");
            filterChain.doFilter(request, response);
            return;
        }

        log.info("현재 URI : " + requestURI);

        boolean isUserActive = userActiveToken.stream()
            .anyMatch(token -> token.endsWith("/*") && requestURI.startsWith(token.substring(0, token.length() - 2)));
        boolean isAdminActive = adminActiveToken.stream()
            .anyMatch(token -> token.endsWith("/*") && requestURI.startsWith(token.substring(0, token.length() - 2)));

        // 토큰이 필요없는 경우, 권한이 필요없는 요청 
        if(!isUserActive && !isAdminActive){
            log.info("권한이 필요없는 요청 or 페이지 로딩");
            filterChain.doFilter(request, response);
            return;
        }

        log.info("JWT 토큰이 필요한 URI : " + requestURI);
        
        String accessToken = request.getHeader("Authorization") != null ? request.getHeader("Authorization").substring(7) : null;

        log.info("AccessToken : " + accessToken);

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
            else if(isUserActive && !isUserAuthenticated(authentication)){
                log.info("사용자 권한이 없습니다.");
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "권한이 없습니다.");
                return;
            }

            // 꺼내온 인증정보를 SecurityContext에 저장
            SecurityContextHolder.getContext().setAuthentication(authentication);
            filterChain.doFilter(request, response);
            return;
        } 
        else if(accessToken != null){

            log.info("AccessToken이 있지만 유효하지 않습니다.");
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
                            String newAccessToken = jwtTokenProvider.reGenerateAccessToken(authentication).getAccessToken();

                            // 권한 확인 함수 호출
                            //checkAuthorization(isAdminActive, isUserActive, authentication, response);

                            // AccessToken을 쿠키에 저장
                            Cookie accessTokenCookie = new Cookie("AccessToken", newAccessToken);
                            accessTokenCookie.setHttpOnly(false); // 클라이언트에서 접근 가능하도록 설정
                            //accessTokenCookie.setSecure(true); // HTTPS에서만 전송
                            accessTokenCookie.setPath("/");
                            accessTokenCookie.setMaxAge(30); // 1분 동안 유효
                            response.addCookie(accessTokenCookie);

                            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

                            SecurityContextHolder.getContext().setAuthentication(null);
                            return;
                        }
                        // RefreshToken이 유효하지 않다면 로그아웃
                        else {
                            log.info("RefreshToken이 유효하지 않습니다.");
                            log.info("재로그인 필요");
                            SecurityContextHolder.getContext().setAuthentication(null);

                            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "재로그인 필요");
                            return;
                        }
                    }
                }
            }
        }

        log.info("AccessToken 없음 및 로그인 오류");
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "AccessToken 없음 및 로그인 오류");
        SecurityContextHolder.getContext().setAuthentication(null);
        return;
    }

    private boolean isAdminAuthenticated(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ADMIN"));
    }

    private boolean isUserAuthenticated(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("USER") || grantedAuthority.getAuthority().equals("ADMIN"));
    }


}

