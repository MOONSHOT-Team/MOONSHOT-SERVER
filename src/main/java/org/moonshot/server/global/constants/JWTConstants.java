package org.moonshot.server.global.constants;

public class JWTConstants {

    public static final String USER_ID = "userId";
    public static final Long ACCESS_TOKEN_EXPIRATION_TIME = 60 * 1000L * 1;
    public static final Long REFRESH_TOKEN_EXPIRATION_TIME = 60 * 1000L * 60 * 24 * 7 * 2; 

}
