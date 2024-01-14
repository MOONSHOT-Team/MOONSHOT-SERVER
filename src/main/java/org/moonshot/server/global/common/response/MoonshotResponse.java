package org.moonshot.server.global.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@JsonPropertyOrder({"status", "message", "data"})
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MoonshotResponse<T> {
    private final int status;
    private final String message;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;

    public static MoonshotResponse<?> success(SuccessType successType) {
        return new MoonshotResponse<>(successType.getHttpStatusCode(), successType.getMessage());
    }

    public static <T> MoonshotResponse<T> success(SuccessType successType, T data) {
        return new MoonshotResponse<>(successType.getHttpStatusCode(), successType.getMessage(), data);
    }

    public static MoonshotResponse<?> error(ErrorType errorType) {
        return new MoonshotResponse<>(errorType.getHttpStatusCode(), errorType.getMessage());
    }

    public static <T> MoonshotResponse<T> error(ErrorType errorType, T data) {
        return new MoonshotResponse<>(errorType.getHttpStatusCode(), errorType.getMessage(), data);
    }

    public static MoonshotResponse<?> error(ErrorType errorType, String message) {
        return new MoonshotResponse<>(errorType.getHttpStatusCode(), message);
    }

    public static <T> MoonshotResponse<T> error(ErrorType errorType, String message, T data) {
        return new MoonshotResponse<>(errorType.getHttpStatusCode(), message, data);
    }

    public static <T> MoonshotResponse<Exception> error(ErrorType errorType, Exception e) {
        return new MoonshotResponse<>(errorType.getHttpStatusCode(), errorType.getMessage(), e);
    }

}
