package com.example.api_gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    /*
        Di uri, karena pakai registry, makanya pakai lb
        kalau ga ada registry, pakai http
     */
    @Bean
    public RouteLocator routeLocator (RouteLocatorBuilder builder){
        return builder.routes()
                .route("auth-route",r -> r
                        .path("/auth/**")
                        .uri("lb://auth-service"))
                .route("product-route", r -> r
                        .path("/product/**")
                        .filters(f -> f.addRequestHeader("X-Source", "gateway"))
                        .uri("lb://product-service"))
                .build();
    }
}
