package org.moonshot.config;

import lombok.RequiredArgsConstructor;
import org.moonshot.constants.WhiteListConstants;
import org.moonshot.filter.MoonshotExceptionHandler;
import org.moonshot.security.JwtAuthenticationFilter;
import org.moonshot.security.JwtExceptionFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final MoonshotExceptionHandler moonshotExceptionHandler;
    private final JwtExceptionFilter jwtExceptionFilter;

    @Value("${server.ip}")
    private String serverIp;
    @Value("${server.domain}")
    private String serverDomain;
    @Value("${client.domain.a}")
    private String clientDomainA;
    @Value("${client.domain.b}")
    private String clientDomainB;
    @Value("${client.domain.c}")
    private String clientDomainC;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .cors(corsConfigurer -> corsConfigurer.configurationSource(corsConfigurationSource()))
                .sessionManagement(sessionManagementConfigurer ->
                        sessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(exceptionHandlingConfigurer ->
                        exceptionHandlingConfigurer.authenticationEntryPoint(moonshotExceptionHandler))
                .exceptionHandling(exceptionHandlingConfigurer ->
                        exceptionHandlingConfigurer.accessDeniedHandler(moonshotExceptionHandler))
                .authorizeHttpRequests(authorizationManagerRequestMatcherRegistry ->
                        authorizationManagerRequestMatcherRegistry.requestMatchers(WhiteListConstants.SECURITY_WHITE_LIST).permitAll())
                .authorizeHttpRequests(authorizationManagerRequestMatcherRegistry ->
                        authorizationManagerRequestMatcherRegistry.anyRequest().authenticated())
                .authorizeHttpRequests(authorizationManagerRequestMatcherRegistry ->
                        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class))
                .authorizeHttpRequests(authorizationManagerRequestMatcherRegistry ->
                        http.addFilterBefore(jwtExceptionFilter, JwtAuthenticationFilter.class))
                .build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("http://localhost:8080");
        config.addAllowedOrigin("http://localhost:5173");
        config.addAllowedOrigin("https://kauth.kakao.com");
        config.addAllowedOrigin("https://kapi.kakao.com");
        config.addAllowedOrigin("http://www.googleapis.com");
        config.addAllowedOrigin("https://www.googleapis.com");
        config.addAllowedOrigin(serverIp);
        config.addAllowedOrigin(serverDomain);
        config.addAllowedOrigin(clientDomainA);
        config.addAllowedOrigin(clientDomainB);
        config.addAllowedOrigin(clientDomainC);
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        config.addExposedHeader("Authorization");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

}
