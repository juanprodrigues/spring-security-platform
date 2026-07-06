package com.api.gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {

        // 1. CREAS EL BUILDER INTERNO
        RouteLocatorBuilder.Builder routesBuilder = builder.routes();

        // 2. DEFINES CADA ROUTE COMO VARIABLE (DEBUG FRIENDLY)

        RouteLocatorBuilder.Builder authRoute =
                routesBuilder.route("auth-service", r ->
                        r.path("/auth/**")
                         .uri("http://localhost:8081")
                );

        RouteLocatorBuilder.Builder productosRoute =
                authRoute.route("productos-service", r ->
                        r.path("/productos/**")
                         .uri("http://localhost:8082")
                );

        RouteLocatorBuilder.Builder clientesRoute =
                productosRoute.route("clientes-service", r ->
                        r.path("/clientes/**")
                         .uri("http://localhost:8083")
                );

        RouteLocatorBuilder.Builder pedidosRoute =
                clientesRoute.route("pedidos-service", r ->
                        r.path("/pedidos/**")
                         .uri("http://localhost:8084")
                );

        // 3. BUILD FINAL DEL ROUTE LOCATOR
        RouteLocator routeLocator = pedidosRoute.build();

        return routeLocator;
    }
}