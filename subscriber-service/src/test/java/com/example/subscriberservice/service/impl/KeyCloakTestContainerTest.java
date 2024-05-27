package com.example.subscriberservice.service.impl;

import dasniko.testcontainers.keycloak.KeycloakContainer;
import io.restassured.RestAssured;
import jakarta.annotation.PostConstruct;
import org.junit.Test;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.apache.http.client.utils.URIBuilder;

import java.net.URI;
import java.util.Map;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertTrue;
@Testcontainers
public interface KeyCloakTestContainerTest {


  @Container
  KeycloakContainer keycloak = new KeycloakContainer().withRealmImportFile("realm-export.json");


  @DynamicPropertySource
  static void registerResourceServerIssuerProperty(DynamicPropertyRegistry registry) {
    registry.add("spring.security.oauth2.client.provider.my-provider.issuer-uri",
            () -> keycloak.getAuthServerUrl() + "/realms/test");

  }




/*  @Container
  KeycloakContainer KEYCLOAK = new KeycloakContainer("quay.io/keycloak/keycloak:24.0.1").withRealmImportFile("/realm-export.json");

  @Test
  public void testKc(){
    assertTrue(KEYCLOAK.isRunning());

    String authServerUrl = KEYCLOAK.getAuthServerUrl();
    System.out.println(authServerUrl);

    String accessToken = given()
            .contentType("application/x-www-form-urlencoded")
            .formParams(Map.of(
                    "username","john",
                    "password","john",
                    "grant_type","password",
                    "client_id","x",
                    "clientSecret","wFVyBi2Jw3ZfyNOnQ79G9m6h4Zn8j6wf"
            ))
            .post(authServerUrl + "realms/test/protocol/openid-connect/certs")
            .then().assertThat().statusCode(200)
            .extract().path("access_token");

    System.out.println(accessToken);

  }*/

}