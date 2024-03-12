/*
package com.example.gatewayservice.config;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.cloud.gateway.filter.factory.GatewayFilterFactory;
import org.springframework.stereotype.Component;

@Component
public class MyFilterFactory extends AbstractGatewayFilterFactory<MyFilterFactory.Config> {

    private final JwtUtil jwtUtil;
    private final RouterValidator routerValidator;

    public MyFilterFactory(JwtUtil jwtUtil, RouterValidator routerValidator) {
        super(Config.class);
        this.jwtUtil = jwtUtil;
        this.routerValidator = routerValidator;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return new AuthenticationFilter(jwtUtil, routerValidator);
    }

    public static class Config {
        // this can be empty if you don't need to pass any filter arguments
    }
}
*/
