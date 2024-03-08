package org.moonshot.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor(access =  AccessLevel.PRIVATE)
public enum SuccessType {

    /**
     * 200 OK (2000 ~ 2099)
     */
    OK(HttpStatus.OK, 2000, "성공"),
    GET_PRESIGNED_URL_SUCCESS(HttpStatus.OK, 2001, "Presigned Url 조회에 성공하였습니다."),
    GET_OKR_LIST_SUCCESS(HttpStatus.OK, 2002, "O-KR 목록 조회에 성공하였습니다."),
    GET_HISTORY_SUCCESS(HttpStatus.OK, 2003, "히스토리 조회에 성공하였습니다."),
    POST_LOGIN_SUCCESS(HttpStatus.OK, 2004, "로그인에 성공하였습니다."),
    POST_REISSUE_SUCCESS(HttpStatus.OK, 2005, "엑세스 토큰 재발급에 성공하였습니다."),
    POST_LOGOUT_SUCCESS(HttpStatus.OK, 2006, "로그아웃에 성공하였습니다."),
    GET_PROFILE_SUCCESS(HttpStatus.OK, 2007, "사용자 프로필 조회에 성공하였습니다."),
    GET_KR_DETAIL_SUCCESS(HttpStatus.OK, 2008, "KR 상세 조회에 성공하였습니다."),
    PATCH_KR_ACHIEVE_SUCCESS(HttpStatus.OK, 2009, "KeyResult 수정 후 목표를 달성하였습니다."),
    POST_LOG_ACHIEVE_SUCCESS(HttpStatus.OK, 2010, "체크인 Log 생성 후 목표를 달성하였습니다."),
    DELETE_OBJECTIVE_SUCCESS(HttpStatus.OK, 2011, "Objective 삭제를 성공하였습니다."),

    /**
     * 201 CREATED (2100 ~ 2199)
     */
    POST_NOTIFY_IMAGE_SAVE_SUCCESS(HttpStatus.CREATED, 2100, "Presigned Url을 통해 이미지 생성을 성공하였습니다."),
    POST_OKR_SUCCESS(HttpStatus.CREATED, 2101, "O-KR을 생성을 성공하였습니다."),
    POST_KEY_RESULT_SUCCESS(HttpStatus.CREATED, 2102, "KeyResult 생성을 성공하였습니다."),
    POST_TASK_SUCCESS(HttpStatus.CREATED, 2103, "Task 생성을 성공하였습니다."),
    POST_LOG_SUCCESS(HttpStatus.CREATED, 2104, "체크인 Log 생성을 성공하였습니다."),

    /**
     * 204 NO CONTENT (2400 ~ 2499)
     */
    PATCH_PROFILE_SUCCESS(HttpStatus.NO_CONTENT, 2400, "사용자 프로필 업데이트에 성공하였습니다."),
    PATCH_OBJECTIVE_SUCCESS(HttpStatus.OK, 2401, "Objective 수정에 성공하였습니다"),
    PATCH_KEY_RESULT_SUCCESS(HttpStatus.NO_CONTENT, 2402, "KeyResult 수정을 성공하였습니다."),
    PATCH_TARGET_INDEX_SUCCESS(HttpStatus.NO_CONTENT, 2403, "대상의 순서 수정을 성공하였습니다."),
    DELETE_KEY_RESULT_SUCCESS(HttpStatus.NO_CONTENT, 2404, "KeyResult 삭제를 성공하였습니다."),
    DELETE_USER_SUCCESS(HttpStatus.NO_CONTENT, 2405, "회원 탈퇴에 성공하였습니다.");

    private final HttpStatus httpStatus;
    private final int code;
    private final String message;

    public int getHttpStatusCode() {
        return httpStatus.value();
    }

}


