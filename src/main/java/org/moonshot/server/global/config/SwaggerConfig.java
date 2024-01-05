package org.moonshot.server.global.config;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;

import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@SecurityScheme(
        name = "JWT Auth",
        in = SecuritySchemeIn.HEADER,
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer",
        description = "Authorization: Bearer ~"
)
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI(){
        Info info = new Info()
                .title("MOONSHOT API Document") // 말 그대로 스웨거의 제목
                .version("V1") // 표기될 버전( 버전을 쓰는 방법에 대해서는 개발자 모두가 한번 알아보면 좋을거 같아요!!)
                .description("MOONSHOT API 명세서입니다.");  // 제목에 대한 설명입니다!
        return new OpenAPI()
                .addServersItem(new Server().url("/")) //초기
                .components(new Components())
                .info(info);
    }

}