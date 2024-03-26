package com.example.gatewayservice.fallBack;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/fallback")
public class GatewayFallbackController {
    @RequestMapping("/profiles")
    public ResponseEntity<String> getAccount() {
        System.out.println("In profiles fallback");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("There is a problem in ProfilesService, please try after some time");
    }

    @RequestMapping("/posts")
    public ResponseEntity<String> getCustomer() {
        System.out.println("In posts fallback");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("There is a problem in PostsService, please try after some time");
    }
}