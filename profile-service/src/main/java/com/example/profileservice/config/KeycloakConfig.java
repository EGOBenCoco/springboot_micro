package com.example.profileservice.config;

import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KeycloakConfig {

    // Замените эти значения на свои настройки Keycloak
    private String serverUrl = "http://localhost:8080";
    private String realm = "master";
    private String clientId = "x";
    private String clientSecret ="5KiBEN4xrckWqT0WORx5QIYCg4Rim7Ac" /*"yXHeGWMOt2eXMkCvMAbI0rxCzj5xbuHMD"*/;

    @Bean
    public Keycloak keycloak() {
        Keycloak keycloak = KeycloakBuilder.builder()
                .serverUrl("http://localhost:8080")
                .realm("test")
                .clientId("admin-cli")
                .username("admin")
                .password("1")
                .resteasyClient(
                        new ResteasyClientBuilder()
                                .connectionPoolSize(10).build()
                ).build();
        return keycloak;
        /*Keycloak.getInstance(
                serverUrl,
                realm,
                clientId,
                clientSecret,
                "admin-cli");*/ // Replace with the appropriate role for your Keycloak setup
    }
}
