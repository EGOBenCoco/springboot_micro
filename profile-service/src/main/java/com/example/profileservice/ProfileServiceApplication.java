package com.example.profileservice;

import com.example.plannerentity.global_exception.CustomException;
import com.example.profileservice.model.Profile;
import com.example.profileservice.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.HttpStatus;

@SpringBootApplication
@ComponentScan(basePackages = {"com.example.plannerentity","com.example.profileservice"})
@EnableJpaRepositories(basePackages = {"com.example.profileservice"})
@EnableFeignClients
@EnableDiscoveryClient
@RequiredArgsConstructor
public class ProfileServiceApplication {
  final  ProfileRepository profileRepository;

    public static void main(String[] args) {
        SpringApplication.run(ProfileServiceApplication.class, args);
    }
    @RabbitListener(queues = "profile")
    public Profile getByAuthId(String authId){
        System.out.println("Profiles aouth id " + authId);
        return profileRepository.findProfileByAuth_id(authId).orElseThrow();
    }
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
