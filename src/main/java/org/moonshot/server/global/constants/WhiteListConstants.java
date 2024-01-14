package org.moonshot.server.global.constants;

import java.util.List;

public class WhiteListConstants {

    public static final List<String> WHITELIST = List.of(
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
