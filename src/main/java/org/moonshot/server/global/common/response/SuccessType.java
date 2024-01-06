package org.moonshot.server.global.common.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor(access =  AccessLevel.PRIVATE)
public enum SuccessType {

    /**
     * 200 OK
     */
    OK(HttpStatus.OK, "성공"),
    GET_PRESIGNED_URL_SUCCESS(HttpStatus.OK, "Presigned Url 조회에 성공하였습니다."),

    /**
     * 201 CREATED
     */
    POST_NOTIFY_IMAGE_SAVE_SUCCESS(HttpStatus.CREATED, "Presigned Url을 통해 이미지 생성을 성공하였습니다."),
    POST_OKR_SUCCESS(HttpStatus.CREATED, "O-KR을 생성을 성공하였습니다."),
    POST_KEY_RESULT_SUCCESS(HttpStatus.CREATED, "KeyResult 생성을 성공하였습니다."),
    POST_TASK_SUCCESS(HttpStatus.CREATED, "Task 생성을 성공하였습니다."),

    /**
     * 204 NO CONTENT
     */
    PATCH_KEY_RESULT_SUCCESS(HttpStatus.NO_CONTENT, "KeyResult 수정을 성공하였습니다."),
    DELETE_KEY_RESULT_SUCCESS(HttpStatus.NO_CONTENT, "KeyResult 삭제를 성공하였습니다."),
    DELETE_OBJECTIVE_SUCCESS(HttpStatus.NO_CONTENT, "Objective 삭제를 성공하였습니다.");

    private final HttpStatus httpStatus;
    private final String message;

    public int getHttpStatusCode() {
        return httpStatus.value();
    }

}


