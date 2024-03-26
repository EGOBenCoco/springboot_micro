package com.example.postservice.producer;

import com.example.plannerentity.dto.responce.PostSubscriberMessage;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class PostProducer {

    private final RabbitTemplate rabbitTemplate;

    public PostProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendPostDataToRabbitMQ(PostSubscriberMessage message) {
        rabbitTemplate.convertAndSend("topic_exchange", "for.subscriber", message);
    }
}