package org.moonshot.server.global.auth.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.moonshot.server.global.auth.exception.InvalidAuthException;
import org.moonshot.server.global.auth.exception.InvalidRefreshTokenException;
import org.moonshot.server.global.common.exception.MoonshotException;
import org.moonshot.server.global.common.response.ErrorType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.Principal;
import java.util.Base64;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import static java.util.Objects.isNull;
import static org.moonshot.server.global.constants.JWTConstants.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    private final RedisTemplate<String, String> redisTemplate;

    @Value("${jwt.secret}")
    private String JWT_SECRET;

    @PostConstruct
    protected void init() {
        JWT_SECRET = Base64.getEncoder().encodeToString(JWT_SECRET.getBytes(StandardCharsets.UTF_8));
    }

    public TokenResponse reissuedToken(Authentication authentication) {
        return TokenResponse.of(
                generateAccessToken(authentication),
                generateRefreshToken(authentication));
    }

    public String generateAccessToken(Authentication authentication) {
        final Date now = new Date();
        final Claims claims = Jwts.claims()
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + ACCESS_TOKEN_EXPIRATION_TIME));

        claims.put(USER_ID, authentication.getPrincipal());

        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setClaims(claims)
                .signWith(getSigningKey())
                .compact();
    }

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
        String encodedKey = Base64.getEncoder().encodeToString(JWT_SECRET.getBytes());
        return Keys.hmacShaKeyFor(encodedKey.getBytes());
    }

    public JwtValidationType validateAccessToken(String token) {
        try {
            final Claims claims = getBody(token);
            return JwtValidationType.VALID_JWT;
        } catch (MalformedJwtException e) {
            throw new MoonshotException(ErrorType.WRONG_TYPE_TOKEN_ERROR);
        } catch (ExpiredJwtException e) {
            throw new MoonshotException(ErrorType.EXPIRED_TOKEN_ERROR);
        } catch (IllegalArgumentException e) {
            throw new MoonshotException(ErrorType.UNKNOWN_TOKEN_ERROR);
        } catch (UnsupportedJwtException e) {
            throw new MoonshotException(ErrorType.UNSUPPORTED_TOKEN_ERROR);
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

    public static Long getUserIdFromPrincipal(Principal principal) {
        if (isNull(principal)) {
            throw new InvalidAuthException();
        }
        return Long.valueOf(principal.getName());
    }

}