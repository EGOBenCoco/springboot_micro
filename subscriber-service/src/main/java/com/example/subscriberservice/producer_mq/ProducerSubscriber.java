package com.example.subscriberservice.producer_mq;

import com.example.plannerentity.config_rabbit.GeneralVariables;
import com.example.plannerentity.dto.responce.PostSubscriberMessage;
import com.example.plannerentity.dto.request.SubscriberEmailRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.messaging.converter.MessageConversionException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class ProducerSubscriber {

    RabbitTemplate rabbitTemplate;
    private static final Logger logger = LoggerFactory.getLogger(ProducerSubscriber.class);
    public void publishCountSubscriber(int profileId, boolean increment) {
        try {
            Map<String, Object> message = new HashMap<>();
            message.put("profileId", profileId);
            message.put("changeCount", increment);
            rabbitTemplate.convertAndSend(GeneralVariables.EXCHANGE, GeneralVariables.PROFILE_KEY, message);
        }
        catch (AmqpException | MessageConversionException e) {
            logger.error("Error publishing profile message", e);
        }
    }
}
