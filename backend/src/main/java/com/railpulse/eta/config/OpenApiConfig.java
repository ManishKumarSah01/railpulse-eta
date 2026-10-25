package com.railpulse.eta.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI railPulseOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("RailPulse ETA API")
                        .description("Dynamic ETA prediction system for coaching trains.")
                        .version("1.0.0"));
    }
}
