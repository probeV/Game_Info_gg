package probeV.GameInfogg.auth.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.stereotype.Component;

import probeV.GameInfogg.auth.dto.response.AccessTokenResponseDto;
import probeV.GameInfogg.auth.dto.response.RefreshTokenResponseDto;

import org.springframework.security.core.Authentication;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;
import java.util.Map;

import javax.crypto.SecretKey;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtTokenProvider implements InitializingBean {
    // JWT 생성 및 검증을 위한 키
    private static final String AUTHORITIES_KEY = "auth";
    private final long accessTokenValidityInMilliseconds;
    private final long refreshTokenValidityInMilliseconds;
    private final String secret;
    
    public JwtTokenProvider(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.token-validity-in-seconds}") long tokenValidityInSeconds
            ) {
        this.secret = secret;
        this.accessTokenValidityInMilliseconds = tokenValidityInSeconds * 60 * 2; // 60,000ms : 1m(0.001d), 60000 * 120 = 2h
        this.refreshTokenValidityInMilliseconds = tokenValidityInSeconds * 60 * 24 * 2; // 60,000ms : 1m(0.001d), 60000 * 60 * 24 * 2 = 2d
    }


    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(this.secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // 액세스 토큰 생성
    @SuppressWarnings("unchecked")
    public AccessTokenResponseDto generateAccessToken(Authentication authentication) {
        // 권한들 가져오기
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        // 토큰의 expire 시간을 설정
        long now = (new Date()).getTime();
        Date accessExprTime = new Date(now + this.accessTokenValidityInMilliseconds);

        log.info("authentication.getPrincipal() : {}", authentication.getPrincipal());

        DefaultOAuth2User defaultOAuth2User = (DefaultOAuth2User) authentication.getPrincipal();
        // 소셜 플렛폼에서의 id 값이 포함된 attribvtes (DefaultOAuth2User 가 usernameAttributeName으로 알아서 가져옴)
        //defaultOAuth2User.getName();
        // 소셜 플렛폼에서의 고유한 id
        Object id=defaultOAuth2User.getName();

        // naver 전용 식별자 체크
        if( id instanceof Map ){
            id=((Map<String, Object>)id).get("id");
        }

        String accessToken = Jwts.builder()
                .subject(id.toString())
                .claim(AUTHORITIES_KEY, authorities)
                .signWith(this.getSigningKey())
                .expiration(accessExprTime)
                .compact();

        return AccessTokenResponseDto.builder()
                .accessToken(accessToken)
                .accessTokenExpiresDate(accessExprTime)
                .build();
    }

    // 액세스 토큰 재발급
    public AccessTokenResponseDto reGenerateAccessToken(Authentication authentication) {
        // 권한들 가져오기
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        log.info("authentication.getPrincipal() : {}", authentication.getPrincipal());

        // 토큰의 expire 시간을 설정
        long now = (new Date()).getTime();
        Date accessExprTime = new Date(now + this.accessTokenValidityInMilliseconds);

        User user = (User) authentication.getPrincipal();
        String subject = user.getUsername();

        String accessToken = Jwts.builder()
                .subject(subject)
                .claim(AUTHORITIES_KEY, authorities)
                .signWith(this.getSigningKey())
                .expiration(accessExprTime)
                .compact();

        return AccessTokenResponseDto.builder()
                .accessToken(accessToken)
                .accessTokenExpiresDate(accessExprTime)
                .build();
    }


    // 리프레시 토큰 생성

    public RefreshTokenResponseDto generateRefreshToken() {
        // 토큰의 expire 시간을 설정
        long now = (new Date()).getTime();
        Date refreshExprTime = new Date(now + this.refreshTokenValidityInMilliseconds);

        String refreshToken = Jwts.builder()
                .signWith(this.getSigningKey())
                .expiration(refreshExprTime)
                .compact();

        return RefreshTokenResponseDto.builder()
                .refreshToken(refreshToken)
                .refreshTokenExpiresDate(refreshExprTime)
                .build();
    }

    // 토큰에서 인증정보를 꺼내옴   
    public Authentication getAuthentication(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(this.getSigningKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            Collection<? extends GrantedAuthority> authorities =
                    Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                            .map(SimpleGrantedAuthority::new)
                            .collect(Collectors.toList());

            User principal = new User(claims.getSubject(), "", authorities);

            return new UsernamePasswordAuthenticationToken(principal, token, authorities);
        } catch (ExpiredJwtException e) {
            Claims claims = e.getClaims();

            Collection<? extends GrantedAuthority> authorities =
                    Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                            .map(SimpleGrantedAuthority::new)
                            .collect(Collectors.toList());

            User principal = new User(claims.getSubject(), "", authorities);

            return new UsernamePasswordAuthenticationToken(principal, token, authorities);
        } 
    }


    // 토큰의 유효성 검증을 수행
    public boolean validateToken(String token) {
        try {
            // 토큰의 유효성 검증 및 Expire 유무를 확인
            Jwts.parser()
                .verifyWith(this.getSigningKey())
                .build()
                .parseSignedClaims(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            log.info("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            log.info("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            log.info("JWT 토큰이 잘못되었습니다.");
        }
        return false;
    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }
}