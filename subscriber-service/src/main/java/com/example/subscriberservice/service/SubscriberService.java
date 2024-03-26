package com.example.subscriberservice.service;

import com.example.plannerentity.dto.request.CountSubscriberMessage;
import com.example.plannerentity.dto.responce.PostSubscriberMessage;
import com.example.subscriberservice.model.Subscriber;
import com.example.subscriberservice.producer.ProducerSubscriber;
import com.example.subscriberservice.repository.SubscriberRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class SubscriberService {

    SubscriberRepository subscriberRepository;
    ProducerSubscriber producerSubscriber;
    public void subscribe(int subscriberId, int profileId, Principal principal) {
        JwtAuthenticationToken token = (JwtAuthenticationToken) principal;
        String email = (String) token.getTokenAttributes().get("email");
        Subscriber existingSubscription = subscriberRepository.findBySubscriberIdAndProfileId(subscriberId, profileId);
     /*   if (existingSubscription != null) {
            throw new IllegalStateException("Вы уже подписаны на этот профиль");
        }*/
        Subscriber subscriber = new Subscriber();
        subscriber.setSubscriberId(subscriberId);
        subscriber.setSubscriberEmail(email);
        subscriber.setProfileId(profileId);
        subscriberRepository.save(subscriber);
        producerSubscriber.publishCountSubscriber(subscriber.getProfileId());

    }

    public List<Subscriber> getSubscriptionsBySubscriberId(int subscriberId) {
        return subscriberRepository.findAllById(subscriberId);
    }

    public List<Subscriber> getSubscriptionsByProfileId(int profileId) {
        return subscriberRepository.findAllByProfileId(profileId);
    }

    @RabbitListener(queues = "subscriber")
    public void receiveMessage(PostSubscriberMessage message) {
        List<Subscriber> subscribers = subscriberRepository.findAllByProfileId(message.getProfileId());
        //System.out.println(subscribers.get(0).getSubscriberEmail());
        for (Subscriber subscriber : subscribers) {
            message.setEmailSubscriber(subscriber.getSubscriberEmail());
            producerSubscriber.publishEmailMessage(message);
        }
    }

 /*   public void unsubscribe(String subscriberId, String profileId) {
        Subscriber subscription = subscriptionRepository.findBySubscriberIdAndProfileId(subscriberId, profileId);
        if (subscription != null) {
            subscriptionRepository.delete(subscription);
        }
    }*/
}
