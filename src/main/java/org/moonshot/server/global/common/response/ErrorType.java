package org.moonshot.server.global.common.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum ErrorType {

     /*
    400 BAD REQUEST
     */
    REQUEST_VALIDATION_EXCEPTION(HttpStatus.BAD_REQUEST, "잘못된 요청입니다"),
    INVALID_TYPE(HttpStatus.BAD_REQUEST, "잘못된 타입이 입력되었습니다."),
    INVALID_MISSING_HEADER(HttpStatus.BAD_REQUEST, "요청에 필요한 헤더값이 존재하지 않습니다."),
    INVALID_HTTP_REQUEST(HttpStatus.BAD_REQUEST, "허용되지 않는 문자열이 입력되었습니다."),
    INVALID_HTTP_METHOD(HttpStatus.BAD_REQUEST, "지원되지 않는 HTTP method 요청입니다."),
    INVALID_IMAGE_EXTENSION(HttpStatus.BAD_REQUEST, "지원하지 않는 이미지 확장자입니다."),
    ACTIVE_OBJECTIVE_NUMBER_EXCEEDED(HttpStatus.BAD_REQUEST, "허용된 Objective 개수를 초과하였습니다"),
    ACTIVE_KEY_RESULT_NUMBER_EXCEEDED(HttpStatus.BAD_REQUEST, "허용된 Key Result 개수를 초과하였습니다"),
    ACTIVE_TASK_NUMBER_EXCEEDED(HttpStatus.BAD_REQUEST, "허용된 Task 개수를 초과하였습니다."),
    INVALID_KEY_RESULT_ORDER(HttpStatus.BAD_REQUEST, "정상적이지 않은 KeyResult 위치입니다."),
    INVALID_TASK_ORDER(HttpStatus.BAD_REQUEST, "정상적이지 않은 Task 위치입니다."),
    INVALID_LOG_VALUE(HttpStatus.BAD_REQUEST, "진척 정도는 이전 값보다 큰 값이 입력되어야합니다."),
    INVALID_EXPIRE_AT(HttpStatus.BAD_REQUEST, "O 수정 기간은 오늘보다 이전 날짜일 수 없습니다."),

    /**
     * 401 UNAUTHROZIED
     */
    INVALID_AUTHORIZATION_ERROR(HttpStatus.UNAUTHORIZED, "유효하지 않은 인증 코드입니다."),
    INVALID_REFRESHTOKEN_ERROR(HttpStatus.UNAUTHORIZED, "유효하지 않은 RefreshToken입니다."),
    INVALID_AUTH_ERROR(HttpStatus.UNAUTHORIZED, "인증되지 않은 사용자입니다."),
    EXPIRED_TOKEN_ERROR(HttpStatus.UNAUTHORIZED, "만료된 TOKEN입니다."),

    /**
     * 403 FORBIDDEN
     */
    FORBIDDEN_ERROR(HttpStatus.FORBIDDEN, "해당 자원에 접근 권한이 없습니다."),

    /**
     * 404 NOT FOUND
     */
    NOT_FOUND_USER_ERROR(HttpStatus.NOT_FOUND, "존재하지 않는 유저입니다."),
    NOT_FOUND_OBJECTIVE_ERROR(HttpStatus.NOT_FOUND, "존재하지 않는 Objective입니다."),
    NOT_FOUND_KEY_RESULT_ERROR(HttpStatus.NOT_FOUND, "존재하지 않는 KeyResult입니다."),
    NOT_FOUND_TASK_ERROR(HttpStatus.NOT_FOUND, "존재하지 않는 Task입니다."),

    /**
     * 500 INTERNAL SERVER ERROR
     */
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "알 수 없는 서버 에러가 발생했습니다"),
    DISCORD_LOG_APPENDER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "디스코드 로그 전송에 실패하였습니다"),
    API_CALL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "외부 API CALL에 실패하였습니다.");

    private final HttpStatus httpStatus;
    private final String message;

    public int getHttpStatusCode() {
        return httpStatus.value();
    }

}
