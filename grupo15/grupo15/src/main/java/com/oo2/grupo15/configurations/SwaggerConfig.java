// src/main/java/com/tuaplicacion/config/SwaggerConfig.java
package com.oo2.grupo15.configurations;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API de Turnito") 
                        .version("1.0.0") 
                        .description("Esta es la documentación de la API para Turnito.") 
                        .termsOfService("http://swagger.io/terms/") 
                        .license(new License().name("Apache 2.0").url("http://springdoc.org"))); 
    }
}