package org.moonshot.server.global.common.exception;

import feign.FeignException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintDefinitionException;
import jakarta.validation.UnexpectedTypeException;
import lombok.extern.slf4j.Slf4j;
import org.moonshot.server.global.common.response.MoonshotResponse;
import org.moonshot.server.global.common.response.ErrorType;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class MoonshotControllerAdvice {

    /**
     * 400 BAD_REQUEST
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public MoonshotResponse handleMethodArgumentNotValidException(final MethodArgumentNotValidException e) {

        Errors errors = e.getBindingResult();
        Map<String, String> validateDetails = new HashMap<>();

        for (FieldError error : errors.getFieldErrors()) {
            String validKeyName = String.format("valid_%s", error.getField());
            validateDetails.put(validKeyName, error.getDefaultMessage());
        }
        log.error(e.getMessage(), e);
        return MoonshotResponse.error(ErrorType.REQUEST_VALIDATION_EXCEPTION, validateDetails);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(UnexpectedTypeException.class)
    public MoonshotResponse<?> handleUnexpectedTypeException(final UnexpectedTypeException e) {
        log.error(e.getMessage(), e);
        return MoonshotResponse.error(ErrorType.INVALID_TYPE);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public MoonshotResponse<?> handlerMethodArgumentTypeMismatchException(final MethodArgumentTypeMismatchException e) {
        log.error(e.getMessage(), e);
        return MoonshotResponse.error(ErrorType.INVALID_TYPE);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MissingRequestHeaderException.class)
    public MoonshotResponse<?> handlerMissingRequestHeaderException(final MissingRequestHeaderException e) {
        log.error(e.getMessage(), e);
        return MoonshotResponse.error(ErrorType.INVALID_MISSING_HEADER);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public MoonshotResponse<?> handlerHttpMessageNotReadableException(final HttpMessageNotReadableException e) {
        log.error(e.getMessage(), e);
        return MoonshotResponse.error(ErrorType.INVALID_HTTP_REQUEST);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public MoonshotResponse<?> handlerHttpRequestMethodNotSupportedException(final HttpRequestMethodNotSupportedException e) {
        log.error(e.getMessage(), e);
        return MoonshotResponse.error(ErrorType.INVALID_HTTP_METHOD);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintDefinitionException.class)
    protected MoonshotResponse<?> handlerConstraintDefinitionException(final ConstraintDefinitionException e) {
        return MoonshotResponse.error(ErrorType.INVALID_HTTP_REQUEST, e.toString());
    }

    /**
     * 401 UNAUTHROZIED
     */
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(FeignException.class)
    public MoonshotResponse<?> handlerFeignException(final FeignException e) {
        log.error(e.getMessage(), e);
        return MoonshotResponse.error(ErrorType.INVALID_AUTHORIZATION_ERROR);
    }

    /**
     * 500 INTERNEL_SERVER
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public MoonshotResponse<?> handleException(final Exception e, final HttpServletRequest request) throws IOException {
        log.error(e.getMessage(), e);
        return MoonshotResponse.error(ErrorType.INTERNAL_SERVER_ERROR);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(IllegalArgumentException.class)
    public MoonshotResponse<?> handlerIllegalArgumentException(final IllegalArgumentException e, final HttpServletRequest request) {
        log.error(e.getMessage(), e);
        return MoonshotResponse.error(ErrorType.INTERNAL_SERVER_ERROR);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(IOException.class)
    public MoonshotResponse<?> handlerIOException(final IOException e, final HttpServletRequest request) {
        log.error(e.getMessage(), e);
        return MoonshotResponse.error(ErrorType.INTERNAL_SERVER_ERROR);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(RuntimeException.class)
    public MoonshotResponse<?> handlerRuntimeException(final RuntimeException e, final HttpServletRequest request) {
        log.error(e.getMessage(), e);
        return MoonshotResponse.error(ErrorType.INTERNAL_SERVER_ERROR);
    }

    /**
     * CUSTOM_ERROR
     */
    @ExceptionHandler(MoonshotException.class)
    public MoonshotResponse<?> handleCustomException(MoonshotException e) {
        return MoonshotResponse.error(e.getErrorType());
    }

}
