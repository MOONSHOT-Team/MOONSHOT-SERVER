package org.moonshot.server.global.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityScheme.Type;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        String jwt = "JWT";
        SecurityRequirement securityRequirement = new SecurityRequirement().addList(jwt);
        Components components = new Components().addSecuritySchemes(jwt, new SecurityScheme()
                .name(jwt)
                .type(Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
        );

        Server productionServer = new Server();
        productionServer.setDescription("production server");
        productionServer.setUrl("https://prod.moonshotyou.com");
        return new OpenAPI()
                .components(components)
                .info(apiInfo())
                .addServersItem(productionServer)
                .addSecurityItem(securityRequirement)
                .components(components);
    }

    private Info apiInfo() {
        return new Info()
                .title("MOONSHOT API Document")
                .version("V1")
                .description("MOONSHOT API 명세서입니다.");
    }

}