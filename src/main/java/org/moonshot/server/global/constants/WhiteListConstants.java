package org.moonshot.server.global.constants;

import java.util.List;

public class WhiteListConstants {

    public static final List<String> WHITELIST = List.of(
            "/login/oauth2/code/kakao",
            "/login/oauth2/code/google",
            "/v1/user/login",
            "/v1/user/googleLogin",
            "/oauth/authorize",
            "user/googleLogin",
            "/login/**",
            "/",
            "/actuator/health",
            "/v1/user/**",
            "/v1/image",
            "/error",
            "/swagger-ui/**",
            "/swagger-resources/**",
            "/api-docs/**"
    );

}
