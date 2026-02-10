package com.dispatch.loadbalancer.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI dispatchOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("Dispatch Load Balancer API")
                        .description("API for optimizing delivery order allocation to vehicle fleets.")
                        .version("v1.0"));
    }
}
