package org.moonshot.server.global.common.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.UnexpectedTypeException;
import lombok.extern.slf4j.Slf4j;
import org.moonshot.server.global.common.response.ApiResponse;
import org.moonshot.server.global.common.response.ErrorType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Component;
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
    protected ApiResponse handleMethodArgumentNotValidException(final MethodArgumentNotValidException e) {

        Errors errors = e.getBindingResult();
        Map<String, String> validateDetails = new HashMap<>();

        for (FieldError error : errors.getFieldErrors()) {
            String validKeyName = String.format("valid_%s", error.getField());
            validateDetails.put(validKeyName, error.getDefaultMessage());
        }
        return ApiResponse.error(ErrorType.REQUEST_VALIDATION_EXCEPTION, validateDetails);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(UnexpectedTypeException.class)
    protected ApiResponse<?> handleUnexpectedTypeException(final UnexpectedTypeException e) {
        return ApiResponse.error(ErrorType.INVALID_TYPE);
    }
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ApiResponse<?> handlerMethodArgumentTypeMismatchException(final MethodArgumentTypeMismatchException e) {
        return ApiResponse.error(ErrorType.INVALID_TYPE);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MissingRequestHeaderException.class)
    protected ApiResponse<?> handlerMissingRequestHeaderException(final MissingRequestHeaderException e) {
        return ApiResponse.error(ErrorType.INVALID_MISSING_HEADER);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    protected ApiResponse<?> handlerHttpMessageNotReadableException(final HttpMessageNotReadableException e) {
        return ApiResponse.error(ErrorType.INVALID_HTTP_REQUEST);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    protected ApiResponse<?> handlerHttpRequestMethodNotSupportedException(final HttpRequestMethodNotSupportedException e) {
        return ApiResponse.error(ErrorType.INVALID_HTTP_METHOD);
    }

    /**
     * 500 INTERNEL_SERVER
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    protected ApiResponse<Exception> handleException(final Exception e, final HttpServletRequest request) throws IOException {
        return ApiResponse.error(ErrorType.INTERNAL_SERVER_ERROR, e);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(IllegalArgumentException.class)
    public ApiResponse<Exception> handlerIllegalArgumentException(final IllegalArgumentException e, final HttpServletRequest request) {
        return ApiResponse.error(ErrorType.INTERNAL_SERVER_ERROR, e);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(IOException.class)
    public ApiResponse<Exception> handlerIOException(final IOException e, final HttpServletRequest request) {
        return ApiResponse.error(ErrorType.INTERNAL_SERVER_ERROR, e);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(RuntimeException.class)
    public ApiResponse<Exception> handlerRuntimeException(final RuntimeException e, final HttpServletRequest request) {
        return ApiResponse.error(ErrorType.INTERNAL_SERVER_ERROR, e);
    }


    /**
     * CUSTOM_ERROR
     */
    @ExceptionHandler(MoonshotException.class)
    protected ResponseEntity<ApiResponse> handleCustomException(MoonshotException e) {
        return ResponseEntity.status(e.getHttpStatus())
                .body(ApiResponse.error(e.getErrorType(), e.getMessage()));
    }
}
