package com.example.subscriberservice.producer;

import com.example.plannerentity.dto.request.CountSubscriberMessage;
import com.example.plannerentity.dto.responce.SubscriberEmailRequest;
import com.example.plannerentity.dto.responce.PostSubscriberMessage;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class ProducerSubscriber {

    RabbitTemplate rabbitTemplate;

    public void publishEmailMessage(PostSubscriberMessage message){

        var emailDTO = new SubscriberEmailRequest(
                message.getEmailSubscriber(),
                "Wet Grapes",
                 "New post by " + message.getAuthorName() + "Name Post" + message.getPostTitle());
        rabbitTemplate.convertAndSend("topic_exchange","for.notification", emailDTO);
    }

    public void publishCountSubscriber(int profileId){
        rabbitTemplate.convertAndSend("topic_exchange","for.profile",profileId);
    }
}
