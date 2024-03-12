package com.example.gatewayservice.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/*@Configuration
public class GatewayConfig {

	@Autowired
	AuthenticationFilter filter;

	@Bean
	public RouteLocator routes(RouteLocatorBuilder builder) {
		return builder.routes()
				.route("customer",
						r -> r.path("/customers/**").filters(f -> f.filter(filter))
								.uri("lb://customer-service"))

				.route("auth-service", r -> r.path("/auth-service/auth/**", "/api/auth/**")
						.filters(f -> f.filter(filter)).uri("lb://auth-service"))
				.build();
	}

}*/
