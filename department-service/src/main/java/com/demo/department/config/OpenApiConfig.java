package com.demo.department.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI departmentOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Department Service API")
                        .description("Microservicio para la gestión de departamentos - SOA Demo")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("SOA Demo")
                                .email("demo@example.com")));
    }
}
