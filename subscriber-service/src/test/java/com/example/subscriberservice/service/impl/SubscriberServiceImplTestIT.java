package com.example.subscriberservice.service.impl;

import com.example.plannerentity.dto.responce.ProfileResponce;
import com.example.subscriberservice.client.ProfileClient;
import com.example.subscriberservice.model.Subscriber;
import com.example.subscriberservice.producer_mq.ProducerSubscriber;
import com.example.subscriberservice.repository.SubscriberRepository;
import com.example.subscriberservice.service.PostgresTestContainer;
import com.example.subscriberservice.service.RabbitTestContainer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.security.Principal;
import java.time.Instant;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
@ExtendWith(SpringExtension.class)
@SpringBootTest(properties = {"spring.config.location=classpath:application-test.properties"},webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations ="classpath:application-test.properties",properties = {"eureka.client.enabled=false"} )
public class SubscriberServiceImplTestIT  implements  PostgresTestContainer,KeyCloakTestContainerTest  {

    @Autowired
    private SubscriberServiceImpl subscriberService;

    @Autowired
    private SubscriberRepository subscriberRepository;


    @MockBean
    private ProfileClient profileClient;

    @Test
    public void testCreateNewSubscriber() {
        int profileId = 1;
        String email = "test@example.com";
        String authId = "auth123";

        // Создание фиктивного токена
        Jwt jwt = Jwt.withTokenValue("dummy-token")
                .header("alg", "none")
                .claim("sub", authId)
                .claim("email", email)
                .build();

        // Установка фиктивного токена в контекст безопасности
        SecurityContextHolder.getContext().setAuthentication(new JwtAuthenticationToken(jwt, null));


        // Установка тестового токена в контекст безопасности
        JwtAuthenticationToken token = new JwtAuthenticationToken(jwt);
        SecurityContextHolder.getContext().setAuthentication(token);

        // Настройка Mock-объектов
        when(profileClient.getById(profileId))
                .thenReturn(new ProfileResponce());

        // Вызов тестируемого метода
        subscriberService.createNewSubscriber(profileId);

        // Проверка ожидаемых результатов
        Subscriber savedSubscriber = subscriberRepository.findBySubscriberEmail(email).orElse(null);
        assertNotNull(savedSubscriber);
        assertEquals(email, savedSubscriber.getSubscriberEmail());
        assertEquals(List.of(profileId), savedSubscriber.getProfileId());
    }
}
