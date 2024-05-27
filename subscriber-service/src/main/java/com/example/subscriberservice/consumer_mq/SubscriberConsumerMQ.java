package com.example.subscriberservice.consumer_mq;

import com.example.plannerentity.config_rabbit.GeneralVariables;
import com.example.plannerentity.dto.request.SubscriberEmailRequest;
import com.example.plannerentity.dto.responce.PostSubscriberMessage;
import com.example.plannerentity.global_exception.ListenerException;
import com.example.subscriberservice.model.Subscriber;
import com.example.subscriberservice.producer_mq.ProducerSubscriber;
import com.example.subscriberservice.repository.SubscriberRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SubscriberConsumerMQ {
    SubscriberRepository subscriberRepository;
    RabbitTemplate rabbitTemplate;


    @RabbitListener(queues = GeneralVariables.QUEUE_SUBSCRIBER)
    public void receiveMessage(PostSubscriberMessage message) throws ListenerException {
        List<Subscriber> subscribers = subscriberRepository.findAllByProfileId(message.getProfileId());
        if (subscribers.isEmpty()) {
            throw new ListenerException("No subscribers found for profile ID: " + message.getProfileId());
        }
        for (Subscriber subscriber : subscribers) {
            message.setEmailSubscriber(subscriber.getSubscriberEmail());
            var emailDTO = new SubscriberEmailRequest(
                    message.getEmailSubscriber(),
                    "Wet Grapes",
                    "New post by " + message.getAuthorName() + " Name Post " + message.getPostTitle());
            rabbitTemplate.convertAndSend(GeneralVariables.EXCHANGE, GeneralVariables.NOTIFICATION_KEY, emailDTO);
        }
    }
}
