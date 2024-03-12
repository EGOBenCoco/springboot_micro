package com.example.postservice.config;

import org.keycloak.admin.client.Keycloak;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KeycloakConfig {

    // Замените эти значения на свои настройки Keycloak
    private String serverUrl = "http://localhost:8080/auth";
    private String realm = "master";
    private String clientId = "x";
    private String clientSecret = "yXHeGWMOt2eXMkCvMAbI0rxCzj5xbuHMD";

    @Bean
    public Keycloak keycloak() {
        return Keycloak.getInstance(
                serverUrl,
                realm,
                clientId,
                clientSecret,
                "admin-cli"); // Replace with the appropriate role for your Keycloak setup
    }
}
