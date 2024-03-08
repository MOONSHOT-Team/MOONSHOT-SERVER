package org.moonshot.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.moonshot.exception.MoonshotException;
import org.moonshot.response.ErrorType;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtExceptionFilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        response.setCharacterEncoding("utf-8");
        try {
            filterChain.doFilter(request, response);
        } catch (MoonshotException e) {
            if(e.getErrorType().equals(ErrorType.UNKNOWN_TOKEN)) {
                setErrorResponse(response, ErrorType.UNKNOWN_TOKEN);
            } else if(e.getErrorType().equals(ErrorType.WRONG_TYPE_TOKEN)) {
                setErrorResponse(response, ErrorType.WRONG_TYPE_TOKEN);
            } else if(e.getErrorType().equals(ErrorType.EXPIRED_TOKEN)) {
                setErrorResponse(response, ErrorType.EXPIRED_TOKEN);
            } else if(e.getErrorType().equals(ErrorType.UNSUPPORTED_TOKEN)) {
                setErrorResponse(response, ErrorType.UNSUPPORTED_TOKEN);
            } else if(e.getErrorType().equals(ErrorType.WRONG_SIGNATURE_TOKEN)) {
                setErrorResponse(response, ErrorType.WRONG_SIGNATURE_TOKEN);
            }
        }
    }

    private void setErrorResponse(HttpServletResponse response, ErrorType errorType) {
        response.setStatus(errorType.getHttpStatus().value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        ErrorResponse errorResponse = new ErrorResponse(errorType.getHttpStatusCode(), errorType.getCode(), errorType.getMessage());
        try {
            response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Data
    public static class ErrorResponse {
        private final Integer status;
        private final int code;
        private final String message;
    }

}
