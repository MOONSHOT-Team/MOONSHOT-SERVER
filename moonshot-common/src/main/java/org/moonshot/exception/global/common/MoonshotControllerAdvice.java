package org.moonshot.exception.global.common;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintDefinitionException;
import jakarta.validation.UnexpectedTypeException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.moonshot.response.ErrorType;
import org.moonshot.response.MoonshotResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@Slf4j
@RestControllerAdvice
public class MoonshotControllerAdvice {

    /**
     * 400 BAD_REQUEST
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<MoonshotResponse<?>> handleMethodArgumentNotValidException(final MethodArgumentNotValidException e) {

        Errors errors = e.getBindingResult();
        Map<String, String> validateDetails = new HashMap<>();

        for (FieldError error : errors.getFieldErrors()) {
            String validKeyName = String.format("valid_%s", error.getField());
            validateDetails.put(validKeyName, error.getDefaultMessage());
        }
        log.error(e.getMessage(), e);
        return new ResponseEntity<>(MoonshotResponse.error(ErrorType.REQUEST_VALIDATION_EXCEPTION, validateDetails), e.getStatusCode());
    }

    @ExceptionHandler(UnexpectedTypeException.class)
    public ResponseEntity<MoonshotResponse<?>> handleUnexpectedTypeException(final UnexpectedTypeException e) {
        log.error(e.getMessage(), e);
        return new ResponseEntity<>(MoonshotResponse.error(ErrorType.INVALID_TYPE), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<MoonshotResponse<?>> handlerMethodArgumentTypeMismatchException(final MethodArgumentTypeMismatchException e) {
        log.error(e.getMessage(), e);
        return new ResponseEntity<>(MoonshotResponse.error(ErrorType.INVALID_TYPE), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MissingRequestHeaderException.class)
    public ResponseEntity<MoonshotResponse<?>> handlerMissingRequestHeaderException(final MissingRequestHeaderException e) {
        log.error(e.getMessage(), e);
        return new ResponseEntity<>(MoonshotResponse.error(ErrorType.INVALID_MISSING_HEADER), e.getStatusCode());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<MoonshotResponse<?>> handlerHttpMessageNotReadableException(final HttpMessageNotReadableException e) {
        log.error(e.getMessage(), e);
        return new ResponseEntity<>(MoonshotResponse.error(ErrorType.INVALID_HTTP_REQUEST), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<MoonshotResponse<?>> handlerHttpRequestMethodNotSupportedException(final HttpRequestMethodNotSupportedException e) {
        log.error(e.getMessage(), e);
        return new ResponseEntity<>(MoonshotResponse.error(ErrorType.INVALID_HTTP_METHOD), e.getStatusCode());
    }

    @ExceptionHandler(ConstraintDefinitionException.class)
    protected ResponseEntity<MoonshotResponse<?>> handlerConstraintDefinitionException(final ConstraintDefinitionException e) {
        return new ResponseEntity<>(MoonshotResponse.error(ErrorType.INVALID_HTTP_REQUEST, e.toString()), HttpStatus.BAD_REQUEST);
    }

    /**
     * 401 UNAUTHROZIED
     */
//    @ExceptionHandler(FeignException.class)
//    public ResponseEntity<MoonshotResponse<?>> handlerFeignException(final FeignException e) {
//        log.error(e.getMessage(), e);
//        return new ResponseEntity<>(MoonshotResponse.error(ErrorType.INVALID_AUTHORIZATION_ERROR), HttpStatus.UNAUTHORIZED);
//    }

    /**
     * 500 INTERNEL_SERVER
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<MoonshotResponse<?>> handleException(final Exception e, final HttpServletRequest request) throws IOException {
        log.error(e.getMessage(), e);
        return new ResponseEntity<>(MoonshotResponse.error(ErrorType.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<MoonshotResponse<?>> handlerIllegalArgumentException(final IllegalArgumentException e, final HttpServletRequest request) {
        log.error(e.getMessage(), e);
        return new ResponseEntity<>(MoonshotResponse.error(ErrorType.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<MoonshotResponse<?>> handlerIOException(final IOException e, final HttpServletRequest request) {
        log.error(e.getMessage(), e);
        return new ResponseEntity<>(MoonshotResponse.error(ErrorType.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<MoonshotResponse<?>> handlerRuntimeException(final RuntimeException e, final HttpServletRequest request) {
        log.error(e.getMessage(), e);
        return new ResponseEntity<>(MoonshotResponse.error(ErrorType.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * CUSTOM_ERROR
     */
    @ExceptionHandler(MoonshotException.class)
    public ResponseEntity<MoonshotResponse<?>> handleCustomException(MoonshotException e) {
        return new ResponseEntity<>(MoonshotResponse.error(e.getErrorType()), HttpStatusCode.valueOf(e.getHttpStatus()));
    }

}
