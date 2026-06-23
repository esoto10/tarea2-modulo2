package com.example.exchange.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI exchangeOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Exchange Rate Service API")
                        .description("REST API for currency exchange rate queries")
                        .version("1.0.0"));
    }
}
