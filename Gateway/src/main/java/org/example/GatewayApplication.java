package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableDiscoveryClient
public class GatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("bookings_route", r -> r
                        .path("/api/v1/bookings/**")
                        .filters(f -> f.stripPrefix(2))
                        .uri("lb://booking-service"))
                .route("users_route", r -> r
                        .path("/api/v1/users/**")
                        .filters(f -> f.stripPrefix(2))
                        .uri("lb://user-service"))
                .route("hotels_route", r -> r
                        .path("/api/v1/hotels/", "/api/v1/rooms/")
                        .filters(f -> f.stripPrefix(2))
                        .uri("lb://hotel-service"))
                .build();
    }

}