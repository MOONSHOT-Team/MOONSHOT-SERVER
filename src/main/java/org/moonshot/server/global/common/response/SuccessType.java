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
    POST_NOTIFY_IMAGE_SAVE_SUCCESS(HttpStatus.CREATED, "Presigned Url을 통해 이미지 생성을 성공하였습니다.");

    /**
     * 204 NO CONTENT
     */

    private final HttpStatus httpStatus;
    private final String message;

    public int getHttpStatusCode() {
        return httpStatus.value();
    }

}


