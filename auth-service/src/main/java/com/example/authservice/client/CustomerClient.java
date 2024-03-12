package com.example.authservice.client;

import com.example.authservice.models.AuthRequest;
import com.example.authservice.models.Customer;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("customer-service")
public interface CustomerClient {
    @PostMapping(value = "/customers", consumes = "application/json", produces = "application/json")
    static Customer create(@RequestBody String x) {
        return null;
    }

    @GetMapping("/customers/getEmail/{email}")
    Customer getByEmail(@PathVariable String email);
}

