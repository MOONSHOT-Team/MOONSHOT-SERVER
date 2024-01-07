package org.moonshot.server.global.config;

import org.moonshot.server.ServerApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackageClasses = ServerApplication.class)
public class FeignConfig {
}
