package com.softwarebhayya.tmbd.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Movie Service API")
                        .description("REST API for managing movies — CRUD operations")
                        .version("v1.0.0")
                        .contact(new Contact()
                                .name("SoftwareBhayya")
                                .email("support@softwarebhayya.com")))
                .servers(List.of(
                        new Server().url("http://localhost:8081").description("Local Dev Server")
                ));
    }
}

