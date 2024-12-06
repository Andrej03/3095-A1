package ca.gbc.eventservice.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {

    @Value("${event-service-version}")
    private String version;

    @Bean
    public OpenAPI eventServiceAPI() {

        return new OpenAPI()
                .info(new Info().title("Event Service API")
                        .description("This is the rest API for Event Service")
                        .version(version)
                        .license(new License().name("Apache 2.0")))
                .externalDocs(new ExternalDocumentation()
                        .description("This is the rest API for Event Service")
                        .url("https://mycompany.ca/event-service/docs"));
    }

}