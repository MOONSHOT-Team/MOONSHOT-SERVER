package org.moonshot.server.global.auth.security;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.moonshot.server.global.auth.exception.ExpiredTokenException;
import org.moonshot.server.global.auth.jwt.JwtTokenProvider;
import org.moonshot.server.global.auth.jwt.JwtValidationType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws IOException, ServletException, IOException {
        try {
            final String token = getJwtFromRequest(request);

            if (jwtTokenProvider.validateAccessToken(token) == JwtValidationType.VALID_JWT) {
                Long userId = jwtTokenProvider.getUserFromJwt(token);
                UserAuthentication authentication = new UserAuthentication(userId, null, null);
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else if (jwtTokenProvider.validateAccessToken(token) == JwtValidationType.EXPIRED_JWT_TOKEN){
                throw new ExpiredTokenException();
            }
        } catch (Exception exception) {
            log.info("Error Occured: ", exception);
        }

        filterChain.doFilter(request, response);
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring("Bearer ".length());
        }
        return null;
    }

}