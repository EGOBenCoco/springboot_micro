package com.example.notificationservice.consumer_mq;

import com.example.notificationservice.service.NotificationService;
import com.example.notificationservice.service.impl.NotificationServiceImpl;
import com.example.plannerentity.dto.request.SubscriberEmailRequest;
import com.example.plannerentity.global_exception.ListenerException;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true)
@Component
public class NotificationConsumerMQ {

    NotificationService notificationServiceImpl;
    @RabbitListener(queues = "notification")
    public void consumer(SubscriberEmailRequest subscriberEmailRequest) throws IOException, ListenerException {
        if(subscriberEmailRequest.emailTo()==null){
            throw new ListenerException("Email is empty");
        }
        notificationServiceImpl.sendTextEmail(subscriberEmailRequest);
    }
}
