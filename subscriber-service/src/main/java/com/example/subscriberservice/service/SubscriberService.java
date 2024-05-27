package com.example.subscriberservice.service;

import com.example.plannerentity.dto.responce.SubscriberResponce;
import org.springframework.data.domain.Page;

import java.security.Principal;
import java.util.List;

public interface SubscriberService {
    void createNewSubscriber(int profileId);

    void addSubscriptionToExistingSubscriber(int profileId);

    Page<SubscriberResponce> getSubscriberSubscriptions(int subscriberId,int page,int size);

    void unsubscribe(int subscriberId, int profileId);
}
