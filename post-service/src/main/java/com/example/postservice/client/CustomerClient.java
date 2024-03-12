package com.example.postservice.client;

import com.example.plannerentity.dto.responce.CustomerResponce;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("customer-service")
public interface CustomerClient {
    @GetMapping("/customers/{id}")
    CustomerResponce getById(@PathVariable int id);
}
