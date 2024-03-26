package com.example.profileservice.consumer_mq;

import com.example.profileservice.model.Profile;
import com.example.profileservice.repository.ProfileRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class ProfileConsumer {
    ProfileRepository profileRepository;

    @RabbitListener(queues = "profile")
    public void countSubscriber(int profileId){
       Profile profile = profileRepository.findById(profileId).orElseThrow();
       BigDecimal count = BigDecimal.valueOf(0);
       profile.setCountSubscriber(count.add(BigDecimal.ONE));
       profileRepository.save(profile);
    }
}
