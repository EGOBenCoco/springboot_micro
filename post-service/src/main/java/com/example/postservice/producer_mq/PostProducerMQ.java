package com.example.postservice.producer_mq;

import com.example.plannerentity.config_rabbit.GeneralVariables;
import com.example.plannerentity.dto.responce.PostSubscriberMessage;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.messaging.converter.MessageConversionException;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class PostProducerMQ {

    private final RabbitTemplate rabbitTemplate;
    private static final Logger logger = LoggerFactory.getLogger(PostProducerMQ.class);

    public void sendPostDataToRabbitMQ(PostSubscriberMessage message) {
        try {
            rabbitTemplate.convertAndSend(GeneralVariables.EXCHANGE, GeneralVariables.SUBSCRIBER_KEY, message);
        }
         catch (AmqpException | MessageConversionException e) {
            logger.error("Error publishing post message", e);
        }
    }
}