package org.moonshot.server.global.constants;

public class JWTConstants {

    public static final String USER_ID = "userId";
    public static final Long ACCESS_TOKEN_EXPIRATION_TIME = 60 * 1000L * 20;  // 액세스 토큰 만료 시간: 20분으로 지정
    public static final Long REFRESH_TOKEN_EXPIRATION_TIME = 60 * 1000L * 60 * 24 * 7 * 2;  // 리프레시 토큰 만료 시간: 2주로 지정

}
