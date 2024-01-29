package org.moonshot.config;

import org.moonshot.ExternalRoot;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackageClasses = ExternalRoot.class)
public class FeignConfig {
}
