package com.example.profileservice.consumer_mq;

import com.example.plannerentity.config_rabbit.GeneralVariables;
import com.example.plannerentity.global_exception.ListenerException;
import com.example.profileservice.model.Profile;
import com.example.profileservice.repository.ProfileRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Map;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class ProfileConsumerMQ {
    ProfileRepository profileRepository;
    @RabbitListener(queues = GeneralVariables.QUEUE_PROFILE)
    public void updateSubscriber(Map<String, Object> message) throws ListenerException {
        int profileId = (int) message.get("profileId");
        boolean increment = (boolean) message.get("changeCount");
        Profile profile = profileRepository.findById(profileId).orElseThrow(()->new ListenerException("Not found"));
        BigDecimal count = profile.getCountSubscriber() != null ? profile.getCountSubscriber() : BigDecimal.ZERO;
        if (increment) {
            profile.setCountSubscriber(count.add(BigDecimal.ONE));
        } else {
            profile.setCountSubscriber(count.subtract(BigDecimal.ONE));
        }
        profileRepository.save(profile);
    }
}
