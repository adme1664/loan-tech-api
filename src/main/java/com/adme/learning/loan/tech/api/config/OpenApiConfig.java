package com.adme.learning.loan.tech.api.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * OpenAPI configuration for the Loan Tech API.
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI loanTechApiOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Loan Tech API")
                        .description("REST API for managing commercial loan agreements, balances, interest rates, and parties")
                        .version("0.0.1-SNAPSHOT")
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0.html")));
    }
}
