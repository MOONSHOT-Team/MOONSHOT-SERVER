package org.moonshot.server.global.auth.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.moonshot.server.domain.user.exception.UserNotFoundException;
import org.moonshot.server.global.auth.exception.InvalidRefreshTokenException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.repository.CrudRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    private static final String USER_ID = "userId";
    private static final Long ACCESS_TOKEN_EXPIRATION_TIME = 60 * 1000L;  // 액세스 토큰 만료 시간: 1분으로 지정
    private static final Long REFRESH_TOKEN_EXPIRATION_TIME = 60 * 1000L * 2;  // 리프레시 토큰 만료 시간: 2분으로 지정
    private final RedisTemplate<String, String> redisTemplate;

    @Value("${jwt.secret}")
    private String JWT_SECRET;

    @PostConstruct
    protected void init() {
        JWT_SECRET = Base64.getEncoder().encodeToString(JWT_SECRET.getBytes(StandardCharsets.UTF_8));
    }

    // Access 토큰, Refresh 토큰 발급
    public TokenResponse reissuedToken(Authentication authentication) {
        return TokenResponse.of(
                generateAccessToken(authentication),
                generateRefreshToken(authentication));
    }

    // 엑세스 토큰 발급
    public String generateAccessToken(Authentication authentication) {
        final Date now = new Date();
        final Claims claims = Jwts.claims()
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + ACCESS_TOKEN_EXPIRATION_TIME));      // 만료 시간

        claims.put(USER_ID, authentication.getPrincipal());

        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setClaims(claims)
                .signWith(getSigningKey())
                .compact();
    }

    //리프레쉬 토큰 발급
    public String generateRefreshToken(Authentication authentication) {
        final Date now = new Date();
        final Claims claims = Jwts.claims()
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + REFRESH_TOKEN_EXPIRATION_TIME));

        claims.put(USER_ID, authentication.getPrincipal());

        String refreshToken = Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setClaims(claims)
                .signWith(getSigningKey())
                .compact();

        redisTemplate.opsForValue().set(
                authentication.getName(),
                refreshToken,
                REFRESH_TOKEN_EXPIRATION_TIME,
                TimeUnit.MILLISECONDS
        );
        return refreshToken;
    }

    private SecretKey getSigningKey() {
        String encodedKey = Base64.getEncoder().encodeToString(JWT_SECRET.getBytes()); //SecretKey 통해 서명 생성
        return Keys.hmacShaKeyFor(encodedKey.getBytes());   //일반적으로 HMAC (Hash-based Message Authentication Code) 알고리즘 사용
    }

    public JwtValidationType validateAccessToken(String token) {
        try {
            final Claims claims = getBody(token);
            return JwtValidationType.VALID_JWT;
        } catch (MalformedJwtException ex) {
            return JwtValidationType.INVALID_JWT_TOKEN;
        } catch (ExpiredJwtException ex) {
            return JwtValidationType.EXPIRED_JWT_TOKEN;
        } catch (UnsupportedJwtException ex) {
            return JwtValidationType.UNSUPPORTED_JWT_TOKEN;
        } catch (IllegalArgumentException ex) {
            return JwtValidationType.EMPTY_JWT;
        }
    }

    public Long validateRefreshToken(String refreshToken) {
        Long userId = getUserFromJwt(refreshToken);
        if (redisTemplate.hasKey(String.valueOf(userId))) {
            return userId;
        } else {
            throw new InvalidRefreshTokenException();
        }
    }

    public void deleteRefreshToken(Long userId) {
        if (redisTemplate.hasKey(String.valueOf(userId))) {
            ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
            String refreshToken = valueOperations.get(String.valueOf(userId));
            redisTemplate.delete(refreshToken);
        } else {
            throw new InvalidRefreshTokenException();
        }
    }

    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder().setSigningKey(JWT_SECRET).build().parseClaimsJws(accessToken).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    private Claims getBody(final String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public Long getUserFromJwt(String token) {
        Claims claims = getBody(token);
        return Long.parseLong(claims.get(USER_ID).toString());
    }

}