package com.example.postservice.client;

import com.example.plannerentity.dto.responce.ProfileResponce;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name="profile-service")
public interface ProfileClient {
    @GetMapping("/profiles/{id}")
    ProfileResponce getById(@PathVariable int id);
}
