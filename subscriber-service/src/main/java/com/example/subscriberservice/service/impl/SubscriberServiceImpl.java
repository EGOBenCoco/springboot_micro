package com.example.subscriberservice.service.impl;

import com.example.plannerentity.dto.responce.ProfileResponce;
import com.example.plannerentity.dto.responce.SubscriberResponce;
import com.example.plannerentity.global_exception.CustomException;
import com.example.subscriberservice.client.ProfileClient;
import com.example.subscriberservice.model.Subscriber;
import com.example.subscriberservice.producer_mq.ProducerSubscriber;
import com.example.subscriberservice.repository.SubscriberRepository;
import com.example.subscriberservice.service.SubscriberService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SubscriberServiceImpl implements SubscriberService {

    SubscriberRepository subscriberRepository;
    ProducerSubscriber producerSubscriber;
    ProfileClient client;

    public void createNewSubscriber(int profileId, Principal principal) {
        String email = getEmail(principal);
        String auth_id = getAuth_Id(principal);
        ProfileResponce profileResponce = client.getById(profileId);
        if (auth_id.equals(profileResponce.getAuth_id()) ||
                subscriberRepository.existsBySubscriberEmailAndProfileId(email, profileId)) {
            throw new CustomException("Подписка невозможна", HttpStatus.BAD_REQUEST);
        }
        Subscriber newSubscriber = new Subscriber();
        newSubscriber.setSubscriberEmail(email);
        newSubscriber.setProfileId(List.of(profileId));
        producerSubscriber.publishCountSubscriber(profileId, true);
        subscriberRepository.save(newSubscriber);
    }

    public void addSubscriptionToExistingSubscriber(int profileId, Principal principal) {
        String email = getEmail(principal);
        Subscriber existingSubscriber = subscriberRepository.findBySubscriberEmail(email).orElseThrow(
                () -> new CustomException("Not found", HttpStatus.NOT_FOUND));
        if (subscriberRepository.existsBySubscriberEmailAndProfileId(email, profileId)) {
            throw new IllegalArgumentException("Подписка уже создана");
        }
        List<Integer> existingProfileIds = existingSubscriber.getProfileId();
        existingProfileIds.add(profileId);
        existingSubscriber.setProfileId(existingProfileIds);
        subscriberRepository.save(existingSubscriber);
        producerSubscriber.publishCountSubscriber(profileId, true);

    }

    public List<SubscriberResponce> getSubscriberSubscriptions(int subscriberId) {
        return subscriberRepository.findById(subscriberId)
                .stream()
                .flatMap(subscriber -> subscriber.getProfileId().stream()
                        .map(profileId -> {
                            ProfileResponce profile = client.getById(profileId);
                            return new SubscriberResponce(subscriber.getId(), profile.getId(), profile.getNickname());
                        })
                )
                .collect(Collectors.toList());
    }

    public void unsubscribe(int subscriberId, int profileId) {
        subscriberRepository.deleteById(subscriberId);
        producerSubscriber.publishCountSubscriber(profileId, false);
    }


    private String getAuth_Id(Principal principal){
        JwtAuthenticationToken token = (JwtAuthenticationToken) principal;
        return (String) token.getTokenAttributes().get("sub");
    }

    private String getEmail(Principal principal){
        JwtAuthenticationToken token = (JwtAuthenticationToken) principal;
        return (String) token.getTokenAttributes().get("email");
    }
}
