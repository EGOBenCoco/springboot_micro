package com.example.profileservice.producer_mq;

import com.example.plannerentity.config_rabbit.GeneralVariables;
import com.example.profileservice.model.Profile;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.messaging.converter.MessageConversionException;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class ProfileProducerMQ {

    RabbitTemplate rabbitTemplate;
    private static final Logger logger = LoggerFactory.getLogger(ProfileProducerMQ.class);

    public void sendMessage(int profileId, String nickname){
        try {
            Map<String, Object> message = new HashMap<>();
            message.put("profileId", profileId);
            message.put("nickname", nickname);
            rabbitTemplate.convertAndSend(GeneralVariables.EXCHANGE, GeneralVariables.POST_KEY, message);
        }
        catch (AmqpException | MessageConversionException e) {
            logger.error("Error publishing post message", e);
        }
    }

}
