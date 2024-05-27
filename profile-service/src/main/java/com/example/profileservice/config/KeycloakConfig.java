package com.example.profileservice.config;

import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KeycloakConfig {

    @Value("${variables.keycloak.serverUrl}")
    private String serverUrl;
    @Value("${variables.keycloak.realm}")
    private String realm;
    @Value("${variables.keycloak.clientId}")

    private String clientId;
    @Value("${variables.keycloak.username}")

    private String username;

    @Value("${variables.keycloak.password}")
    private String password;

    @Bean
    public Keycloak keycloak() {
        Keycloak keycloak = KeycloakBuilder.builder()
                .serverUrl(serverUrl)
                .realm(realm)
                .clientId(clientId)
                .username(username)
                .password(password)
                .resteasyClient(
                        new ResteasyClientBuilder()
                                .connectionPoolSize(10).build()
                ).build();
        return keycloak;
    }
}
