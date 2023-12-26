package org.moonshot.server.global.config;

import static org.springframework.http.HttpStatus.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.moonshot.server.global.common.model.CustomLog;
import org.moonshot.server.global.common.model.Logging;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Slf4j
@Aspect
@Configuration
public class LoggerConfig {

    private static final String format = "yyyy-MM-dd HH:mm:ss.SSS";

    @Around("@annotation(org.moonshot.server.global.common.model.Logging) && @annotation(logging)")
    public Object aroundLogger(ProceedingJoinPoint joinPoint, Logging logging) throws Throwable {
        CustomLog customLog = new CustomLog();
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern(format);
        customLog.setCreatedAt(LocalDateTime.now().format(timeFormatter));

        HttpServletRequest request =
                ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        customLog.setUri(request.getRequestURI());

        Object result = null;
        try {
            result = joinPoint.proceed();
        } catch (Throwable t) {
            throw t;
        } finally {
            if (result instanceof ResponseEntity) {
                ResponseEntity responseEntity = (현ResponseEntity) result;

                if (responseEntity.getStatusCode() == OK || responseEntity.getStatusCode() == CREATED || responseEntity.getStatusCode() == NO_CONTENT) {
                    customLog.setResult("success-" + responseEntity.getStatusCode());
                } else {
                    HttpStatus status = valueOf(responseEntity.getStatusCode().value());
                    customLog.setResult("fail-" + status.getReasonPhrase());
                }
            }

            if (result == null) customLog.setResult("fail");

            MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
            customLog.setMethod(methodSignature.getName());
            customLog.setItem(logging.item());
            customLog.setAction(logging.action());

            log.info(getMessage(customLog));
        }
        return result;
    }

    private String getMessage(CustomLog customLog) throws JsonProcessingException {
        Map<String, String> map = new LinkedHashMap<>();

        map.put("createdAt", customLog.getCreatedAt());
        map.put("item", customLog.getItem());
        map.put("action", customLog.getAction());
        map.put("result", customLog.getResult());
        map.put("uri", customLog.getUri());
        map.put("method", customLog.getMethod());

        return new ObjectMapper().writeValueAsString(map);
    }
}
