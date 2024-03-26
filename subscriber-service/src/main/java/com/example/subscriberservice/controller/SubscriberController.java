package com.example.subscriberservice.controller;

import com.example.subscriberservice.model.Subscriber;
import com.example.subscriberservice.service.SubscriberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/subscribers")
@RequiredArgsConstructor
public class SubscriberController {

    private final SubscriberService subscriberService;

    @GetMapping("/test")
    public String test(){
        return "HI";
    }

    @PostMapping("/subscribe/{profileId}/{subscriberId}")
    public ResponseEntity<String> subscribe(@PathVariable int subscriberId, @PathVariable int profileId, Principal principal) {
        subscriberService.subscribe(subscriberId, profileId,principal);
        return ResponseEntity.ok("Subscribed successfully");
    }

    @GetMapping("/subscriptions/{subscriberId}")
    public ResponseEntity<List<Subscriber>> getSubscriptionsBySubscriberId(@PathVariable int subscriberId) {
        List<Subscriber> subscriptions = subscriberService.getSubscriptionsBySubscriberId(subscriberId);
        return ResponseEntity.ok(subscriptions);
    }

    @GetMapping("/subscriptions/profile/{profileId}")
    public ResponseEntity<List<Subscriber>> getSubscriptionsByProfileId(@PathVariable int profileId) {
        List<Subscriber> subscriptions = subscriberService.getSubscriptionsByProfileId(profileId);
        return ResponseEntity.ok(subscriptions);
    }
}

