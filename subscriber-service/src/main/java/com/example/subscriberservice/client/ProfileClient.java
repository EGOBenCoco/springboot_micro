package com.example.subscriberservice.client;

import com.example.plannerentity.dto.responce.ProfileResponce;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name="profile-service",url = "http://profile-service/")
public interface ProfileClient {
    @GetMapping("/api/v1/profiles/{id}")
    ProfileResponce getById(@PathVariable int id);
}
