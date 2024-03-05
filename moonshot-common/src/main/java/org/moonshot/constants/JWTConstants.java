package org.moonshot.constants;

public class JWTConstants {

    public static final String USER_ID = "userId";
    public static final String TOKEN_TYPE = "type";
    public static final String ACCESS_TOKEN = "access";
    public static final String REFRESH_TOKEN = "refresh";
    public static final Long ACCESS_TOKEN_EXPIRATION_TIME = 60 * 1000L * 20;
    public static final Long REFRESH_TOKEN_EXPIRATION_TIME = 60 * 1000L * 60 * 24 * 7 * 2; 

}