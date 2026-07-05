package com.api.gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {

        return builder.routes()

                .route("auth-service", r -> r
                        .path("/auth/**")
                        .uri("http://localhost:8081"))

                .route("productos-service", r -> r
                        .path("/productos/**")
                        .uri("http://localhost:8082"))

                .route("clientes-service", r -> r
                        .path("/clientes/**")
                        .uri("http://localhost:8083"))

                .route("pedidos-service", r -> r
                        .path("/pedidos/**")
                        .uri("http://localhost:8084"))

                .build();
    }
}