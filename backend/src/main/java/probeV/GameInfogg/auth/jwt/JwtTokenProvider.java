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

import javax.crypto.SecretKey;


@Component
public class JwtTokenProvider implements InitializingBean {
    // JWT 생성 및 검증을 위한 키
    private static final String AUTHORITIES_KEY = "auth";
    private final long accessTokenValidityInMilliseconds;
    private final long refreshTokenValidityInMilliseconds;
    private final String secret;

    public JwtTokenProvider(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.token-validity-in-seconds}") long tokenValidityInSeconds) {
        this.secret = secret;
        this.accessTokenValidityInMilliseconds = tokenValidityInSeconds * 30; // 60,000ms : 1m(0.001d), 60000 * 30 = 30m
        this.refreshTokenValidityInMilliseconds = tokenValidityInSeconds * 60 * 24 * 2; // 60,000ms : 1m(0.001d), 60000 * 60 * 24 * 2 = 2d
    }

    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(this.secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public AccessTokenResponseDto generateAccessToken(Authentication authentication) {
        // 권한들 가져오기
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        // 토큰의 expire 시간을 설정
        long now = (new Date()).getTime();
        Date accessExprTime = new Date(now + this.accessTokenValidityInMilliseconds);

        DefaultOAuth2User defaultOAuth2User = (DefaultOAuth2User) authentication.getPrincipal();
        // 소셜 플렛폼에서의 고유한 id
        Object id = defaultOAuth2User.getAttributes().get("id");
        // 소셜 플렛폼에서의 이름
        String nameAttributeKey = defaultOAuth2User.getName();
        String provider;
        if(nameAttributeKey.equals("respoonse")){
            provider="kakao";
        }
        else{
            provider="naver";
        }

        String accessToken = Jwts.builder()
                .subject(String.valueOf(id))
                .claim(AUTHORITIES_KEY, authorities)
                .claim("provider", provider)
                .signWith(this.getSigningKey())
                .expiration(accessExprTime)
                .compact();

        return AccessTokenResponseDto.builder()
                .accessToken(accessToken)
                .accessTokenExpiresDate(accessExprTime)
                .build();
    }

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
            e.printStackTrace();
            System.out.println("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            e.printStackTrace();
            System.out.println("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            e.printStackTrace();
            System.out.println("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            System.out.println("JWT 토큰이 잘못되었습니다.");
        }
        return false;
    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }
}