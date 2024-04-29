package com.example.gatewayservice.fallBack;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/fallback")
public class GatewayFallbackController {
    @RequestMapping("/api/v1/profiles")
    public ResponseEntity<String> fallbackProfiles() {
        System.out.println("In profiles fallback");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("There is a problem in ProfilesService, please try after some time");
    }

    @RequestMapping("/api/v1/posts")
    public ResponseEntity<String> fallbackPosts() {
        System.out.println("In posts fallback");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("There is a problem in PostsService, please try after some time");
    }

    @RequestMapping("/api/v1/subscribers")
    public ResponseEntity<String> fallbackSubscribers() {
        System.out.println("In subscribers fallback");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("There is a problem in SubscriberService, please try after some time");
    }
}