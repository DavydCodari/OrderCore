package com.davyd.ordercore.common.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI flowBankOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("OrderCore API")
                        .description(
                                "REST API for managing customers, orders, carts, products and payments"
                                        + " with auth functionality"
                        )
                        .version("1.0.0")
                );
    }
}
