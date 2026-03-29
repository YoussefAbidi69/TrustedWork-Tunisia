package tn.esprit.reviewservice.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("TrustedWork Tunisia — Review Service API")
                        .version("1.0.0")
                        .description("Module 4 : Reviews, Reputation (TrustScore), GrowthEngine (XP/Badges), Réclamations — 6 entités, 5 features IA, ~18 endpoints")
                        .contact(new Contact().name("PI Cloud Team").email("picloud@esprit.tn")));
    }
}