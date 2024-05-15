package ru.arman.config;

import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.arman.filter.JwtAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class GatewayConfig {
    private final JwtAuthenticationFilter filter;

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("auth-service", r -> r.path("/api/user/**").filters(f -> f.filter(filter)).uri("lb://auth-service"))
                .route("location-service", r -> r.path("/api/location/**").filters(f -> f.filter(filter)).uri("lb://location-service"))
                .route("location-service", r -> r.path("/api/reservation/**").filters(f -> f.filter(filter)).uri("lb://reservation-service"))
                .route("location-service", r -> r.path("/api/vehicle/**").filters(f -> f.filter(filter)).uri("lb://vehicle-service"))
                .route("discovery-service", r -> r.path("/eureka/web").filters(f -> f.setPath("/")).uri("http://localhost:8761"))
                .route("discovery-service-static", r -> r.path("/eureka/**").uri("http://localhost:8761"))
                .build();
    }
}
