package com.example.subscriberservice.service.impl;

import com.example.plannerentity.dto.responce.ProfileResponce;
import com.example.plannerentity.dto.responce.SubscriberResponce;
import com.example.plannerentity.global_exception.CustomException;
import com.example.subscriberservice.client.ProfileClient;
import com.example.subscriberservice.model.Subscriber;
import com.example.subscriberservice.producer_mq.ProducerSubscriber;
import com.example.subscriberservice.repository.SubscriberRepository;
import com.example.subscriberservice.service.impl.SubscriberServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)

class SubscriberServiceImplTest {
    @Mock
    private SubscriberRepository subscriberRepository;

    @Mock
    private ProducerSubscriber producerSubscriber;

    @Mock
    private ProfileClient profileClient;

    @Mock
    private Authentication authentication;

    @Mock
    private JwtAuthenticationToken jwtAuthenticationToken;

    @InjectMocks
    private SubscriberServiceImpl subscriberService;

    private ProfileResponce profileResponce;

    private Subscriber subscriber;

    @BeforeEach
    public void setup() {
        jwtAuthenticationToken = mock(JwtAuthenticationToken.class);
        when(jwtAuthenticationToken.getTokenAttributes()).thenReturn(Map.of(
                "email", "user@example.com",
                "sub", "auth_id_example"
        ));

        authentication = jwtAuthenticationToken;
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        profileResponce = new ProfileResponce();
        profileResponce.setId(1);
        profileResponce.setNickname("testUser");
        profileResponce.setAuth_id("testAuthId");

        subscriber = new Subscriber();
        subscriber.setSubscriberEmail("user@example.com");
        subscriber.setProfileId(List.of(1));

    }

    @Test
    public void testCreateNewSubscriber() {
        when(profileClient.getById(1)).thenReturn(profileResponce);
        subscriberService.createNewSubscriber(1);

        verify(subscriberRepository, times(1)).existsBySubscriberEmailAndProfileId(eq("user@example.com"), eq(1));
        verify(subscriberRepository, times(1)).save(any(Subscriber.class));
        verify(producerSubscriber, times(1)).publishCountSubscriber(eq(1), eq(true));
    }

    @Test
    public void testAddSubscriptionToExistingSubscriber() {
        when(subscriberRepository.findBySubscriberEmail(eq("user@example.com"))).thenReturn(Optional.of(subscriber));
        when(subscriberRepository.existsBySubscriberEmailAndProfileId(eq("user@example.com"), anyInt())).thenReturn(false);

        subscriberService.addSubscriptionToExistingSubscriber(123);

        verify(subscriberRepository, times(1)).findBySubscriberEmail(eq("user@example.com"));
        verify(subscriberRepository, times(1)).existsBySubscriberEmailAndProfileId(eq("user@example.com"), eq(123));
        verify(subscriberRepository, times(1)).save(subscriber);
        verify(producerSubscriber, times(1)).publishCountSubscriber(eq(123), eq(true));
    }


    @Test
    public void testAddSubscriptionToExistingSubscriber_SubscriberNotFound() {
        when(subscriberRepository.findBySubscriberEmail(anyString())).thenReturn(Optional.empty());

        assertThrows(CustomException.class, () -> {
            subscriberService.addSubscriptionToExistingSubscriber(123);
        });
    }


    @Test
    public void testUnsubscribe() {
            subscriberService.unsubscribe(1, 101);

            verify(subscriberRepository, times(1)).deleteById(eq(1));
            verify(producerSubscriber, times(1)).publishCountSubscriber(eq(101), eq(false));
        }

    @Test
    void getSubscriberSubscriptions() {
        // Arrange
        int subscriberId = 1;
        int page = 0;
        int size = 10;

        Subscriber subscriber = new Subscriber();

        List<Subscriber> subscribers = List.of(subscriber);
        Page<Subscriber> subscriberPage = new PageImpl<>(subscribers, PageRequest.of(page, size), subscribers.size());

        when(subscriberRepository.findById(subscriberId, PageRequest.of(page, size))).thenReturn(subscriberPage);
        when(profileClient.getById(subscriber.getId())).thenReturn(profileResponce);

        Page<SubscriberResponce> result = subscriberService.getSubscriberSubscriptions(subscriberId, page, size);

        List<SubscriberResponce> expectedContent = List.of(
                new SubscriberResponce(subscriberId, profileResponce.getId(), profileResponce.getNickname())
        );
        Page<SubscriberResponce> expectedPage = new PageImpl<>(expectedContent, PageRequest.of(page, size), expectedContent.size());

        assertEquals(expectedPage, result);
    }

/*    @Test
    void getSubscriberSubscriptions() {
        // Arrange
        int subscriberId = 1;
        when(subscriberRepository.findById(subscriberId)).thenReturn(Optional.of(subscriber));
        when(profileClient.getById(1)).thenReturn(profileResponce);

        // Act
        List<SubscriberResponce> result = subscriberService.getSubscriberSubscriptions(subscriberId);

        // Assert
        List<SubscriberResponce> expected = List.of(
                new SubscriberResponce(subscriberId, 1, "testUser"));


        assertEquals(expected, result);
    }*/
}

