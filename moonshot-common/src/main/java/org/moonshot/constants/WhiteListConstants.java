package org.moonshot.constants;

import java.util.List;

public class WhiteListConstants {

    public static final List<String> FILTER_WHITE_LIST = List.of(
            "/login/oauth2/code/kakao",
            "/login/oauth2/code/google",
            "/v1/user/login",
            "/login",
            "/oauth/authorize",
            "/actuator/health",
            "/error",
            "/swagger-ui/",
            "/swagger-resources/",
            "/api-docs/"
    );

    public static final String[] SECURITY_WHITE_LIST = {
            "/login/**",
            "/",
            "/actuator/health",
            "/v1/user/**",
            "/error",
            "/swagger-ui/**",
            "/swagger-resources/**",
            "/api-docs/**",
            "/v3/api-docs/**",
            "/webjars/**"
    };

}
