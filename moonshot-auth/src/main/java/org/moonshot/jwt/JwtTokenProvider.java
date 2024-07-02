package org.moonshot.jwt;

import static org.moonshot.response.ErrorType.*;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.crypto.SecretKey;

import org.moonshot.constants.JWTConstants;
import org.moonshot.exception.UnauthorizedException;
import org.moonshot.security.UserAuthentication;
import org.moonshot.security.service.UserPrincipalDetailsService;
import org.moonshot.user.model.UserPrincipal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    private final RedisTemplate<String, String> redisTemplate;
    private final UserPrincipalDetailsService userPrincipalDetailsService;

    @Value("${jwt.secret}")
    private String JWT_SECRET;

    @PostConstruct
    protected void init() {
        JWT_SECRET = Base64.getEncoder().encodeToString(JWT_SECRET.getBytes(StandardCharsets.UTF_8));
    }

    public TokenResponse reissuedToken(Long userId) {
        return TokenResponse.of(
                generateAccessToken(userId),
                generateRefreshToken(userId));
    }

    public String generateAccessToken(Long userId) {
        final Date now = new Date();
        final Claims claims = Jwts.claims()
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + JWTConstants.ACCESS_TOKEN_EXPIRATION_TIME));

        claims.put(JWTConstants.USER_ID, userId);
        claims.put(JWTConstants.TOKEN_TYPE, JWTConstants.ACCESS_TOKEN);

        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setClaims(claims)
                .signWith(getSigningKey())
                .compact();
    }

    public String generateRefreshToken(Long userId) {
        final Date now = new Date();
        final Claims claims = Jwts.claims()
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + JWTConstants.REFRESH_TOKEN_EXPIRATION_TIME));

        claims.put(JWTConstants.USER_ID, userId);
        claims.put(JWTConstants.TOKEN_TYPE, JWTConstants.REFRESH_TOKEN);

        String refreshToken = Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setClaims(claims)
                .signWith(getSigningKey())
                .compact();

        redisTemplate.opsForValue().set(
                String.valueOf(userId),
                refreshToken,
                JWTConstants.REFRESH_TOKEN_EXPIRATION_TIME,
                TimeUnit.MILLISECONDS
        );
        return refreshToken;
    }

    private SecretKey getSigningKey() {
        String encodedKey = Base64.getEncoder().encodeToString(JWT_SECRET.getBytes());
        return Keys.hmacShaKeyFor(encodedKey.getBytes());
    }

    public Long validateRefreshToken(String refreshToken) {
        validateToken(refreshToken);
        Long userId = getUserFromJwt(refreshToken);
        if (redisTemplate.hasKey(String.valueOf(userId))) {
            return userId;
        } else {
            throw new UnauthorizedException(INVALID_REFRESH_TOKEN);
        }
    }

    public JwtValidationType validateToken(String token) {
        try {
            final Claims claims = getBody(token);
            if (claims.get(JWTConstants.TOKEN_TYPE).toString().equals(JWTConstants.ACCESS_TOKEN)) {
                return JwtValidationType.VALID_ACCESS;
            } else if (claims.get(JWTConstants.TOKEN_TYPE).toString().equals(JWTConstants.REFRESH_TOKEN)) {
                return JwtValidationType.VALID_REFRESH;
            }
            throw new UnauthorizedException(WRONG_TYPE_TOKEN);
        } catch (MalformedJwtException e) {
            throw new UnauthorizedException(WRONG_TYPE_TOKEN);
        } catch (ExpiredJwtException e) {
            throw new UnauthorizedException(EXPIRED_TOKEN);
        } catch (IllegalArgumentException e) {
            throw new UnauthorizedException(UNKNOWN_TOKEN);
        } catch (UnsupportedJwtException e) {
            throw new UnauthorizedException(UNSUPPORTED_TOKEN);
        } catch (SignatureException e) {
            throw new UnauthorizedException(WRONG_SIGNATURE_TOKEN);
        }
    }

    public void deleteRefreshToken(Long userId) {
        redisTemplate.delete(String.valueOf(userId));
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
        return Long.parseLong(claims.get(JWTConstants.USER_ID).toString());
    }

    public Authentication getAuthentication(Long userId) {
        UserPrincipal userDetails = (UserPrincipal) userPrincipalDetailsService.loadUserByUsername(String.valueOf(userId));
        return new UserAuthentication(userDetails);
    }

}