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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SubscriberServiceImpl implements SubscriberService {

    SubscriberRepository subscriberRepository;
    ProducerSubscriber producerSubscriber;
    ProfileClient client;

    public void createNewSubscriber(int profileId) {
        String email = getTokenAttribute("email");
        String auth_id = getTokenAttribute("sub");
        ProfileResponce profileResponce = client.getById(profileId);
        if (auth_id.equals(profileResponce.getAuth_id()) ||
                subscriberRepository.existsBySubscriberEmailAndProfileId(email, profileId)) {
            throw new CustomException("Subscription is not possible", HttpStatus.BAD_REQUEST);
        }
        Subscriber newSubscriber = new Subscriber();
        newSubscriber.setSubscriberEmail(email);
        newSubscriber.setProfileId(List.of(profileId));
        producerSubscriber.publishCountSubscriber(profileId, true);
        subscriberRepository.save(newSubscriber);
    }

    public void addSubscriptionToExistingSubscriber(int profileId) {
        String email = getTokenAttribute("email");
        Subscriber existingSubscriber = subscriberRepository.findBySubscriberEmail(email).orElseThrow(
                () -> new CustomException("Not found", HttpStatus.NOT_FOUND));
        if (subscriberRepository.existsBySubscriberEmailAndProfileId(email, profileId)) {
            throw new IllegalArgumentException("Subscription has already been created");
        }
        List<Integer> existingProfileIds = new ArrayList<>(existingSubscriber.getProfileId());
        //  List<Integer> existingProfileIds = existingSubscriber.getProfileId();
        existingProfileIds.add(profileId);
        existingSubscriber.setProfileId(existingProfileIds);
        subscriberRepository.save(existingSubscriber);
        producerSubscriber.publishCountSubscriber(profileId, true);

    }


    public Page<SubscriberResponce> getSubscriberSubscriptions(int subscriberId, int page, int size) {
        Page<Subscriber> subscriberPage = subscriberRepository.findById(subscriberId, PageRequest.of(page, size));
        List<SubscriberResponce> subscriberResponces = subscriberPage.getContent().stream()
                .map(profileId -> {
                    ProfileResponce profile = client.getById(profileId.getId());
                    return new SubscriberResponce(subscriberId, profile.getId(), profile.getNickname());
                })
                .collect(Collectors.toList());
        return new PageImpl<>(subscriberResponces, subscriberPage.getPageable(), subscriberPage.getTotalElements());
    }

    public void unsubscribe(int subscriberId, int profileId) {
        subscriberRepository.deleteById(subscriberId);
        producerSubscriber.publishCountSubscriber(profileId, false);
    }

    private String getTokenAttribute(String arg) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        JwtAuthenticationToken token = (JwtAuthenticationToken) authentication;
        return (String) token.getTokenAttributes().get(arg);
    }
}
