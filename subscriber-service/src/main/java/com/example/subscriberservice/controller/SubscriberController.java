package com.example.subscriberservice.controller;

import com.example.plannerentity.dto.responce.SubscriberResponce;
import com.example.plannerentity.global_exception.SuccessMessage;
import com.example.subscriberservice.service.SubscriberService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/subscribers")
@RequiredArgsConstructor
public class SubscriberController {

    private final SubscriberService subscriberService;
    @PostMapping("/{profileId}/subscribe")
    public ResponseEntity<Object> subscribe( @PathVariable int profileId) {
        subscriberService.createNewSubscriber( profileId);
        return ResponseEntity.ok(SuccessMessage.builder()
                .status(HttpStatus.CREATED.value())
                .message("Subscribe added")
                .datetime(LocalDateTime.now())
                .build());
    }

    @PostMapping("/{profileId}/profile")
    public ResponseEntity<Object> addNew( @PathVariable int profileId) {
        subscriberService.addSubscriptionToExistingSubscriber( profileId);
        return ResponseEntity.ok(SuccessMessage.builder()
                .status(HttpStatus.CREATED.value())
                .message("Subscribe added")
                .datetime(LocalDateTime.now())
                .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Page<SubscriberResponce>> getSubscriptionsBySubscriberId(@PathVariable int id,
                                                                                   @RequestParam(name = "page", defaultValue = "0") int page,
                                                                                   @RequestParam(name = "size", defaultValue = "10") int size) {
        return ResponseEntity.ok(subscriberService.getSubscriberSubscriptions(id,page,size));
    }

    @DeleteMapping("/{profileId}/{subscriberId}")
    public ResponseEntity<Object> unsubscribe(@PathVariable int subscriberId, @PathVariable int profileId){
        subscriberService.unsubscribe(subscriberId,profileId);
        return ResponseEntity.ok(SuccessMessage.builder()
                .status(HttpStatus.OK.value())
                .message("Subscribe deleted")
                .datetime(LocalDateTime.now())
                .build());
    }
}

