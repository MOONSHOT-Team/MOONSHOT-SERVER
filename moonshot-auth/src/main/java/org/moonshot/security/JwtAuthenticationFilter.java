package org.moonshot.server.global.auth.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.moonshot.constants.WhiteListConstants;
import org.moonshot.jwt.JwtTokenProvider;
import org.moonshot.server.global.auth.jwt.JwtValidationType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws IOException, ServletException, IOException {
        for (String whiteUrl : WhiteListConstants.FILTER_WHITE_LIST) {
            if (request.getRequestURI().contains(whiteUrl)) {
                filterChain.doFilter(request, response);
                return;
            }
        }
        final String token = getJwtFromRequest(request);
        if (jwtTokenProvider.validateAccessToken(token) == JwtValidationType.VALID_JWT) {
            Long userId = jwtTokenProvider.getUserFromJwt(token);
            org.moonshot.server.global.auth.security.UserAuthentication authentication = new org.moonshot.server.global.auth.security.UserAuthentication(userId, null, null);
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);
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