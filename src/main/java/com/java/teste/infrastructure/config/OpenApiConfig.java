package com.java.teste.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.ForwardedHeaderFilter;

@Configuration
public class OpenApiConfig {

        @Bean
        public ForwardedHeaderFilter forwardedHeaderFilter() {
                return new ForwardedHeaderFilter();
        }

    @Bean
    public OpenAPI personApiOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Person API")
                        .description("""
                                API de gerenciamento de pessoas.
                                
                                Funcionalidades:
                                - CRUD completo de pessoas (listar, buscar, criar, atualizar, remover)
                                - Cálculo de idade em dias, meses ou anos
                                - Cálculo de salário atual com base no tempo de empresa
                                """)
                        .version("1.0.0"));
    }
}
