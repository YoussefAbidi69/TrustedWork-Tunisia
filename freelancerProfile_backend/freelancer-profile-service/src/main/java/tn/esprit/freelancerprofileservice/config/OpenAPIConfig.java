package tn.esprit.freelancerprofileservice.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Freelancer Profile Service API")
                        .version("1.0")
                        .description("Module 1 — TrustedWork Tunisia — ESPRIT 2024/2025"));
    }
}