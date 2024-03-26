package com.example.notificationservice.consumer_mq;

import com.example.notificationservice.service.NotificationService;
import com.example.plannerentity.dto.responce.SubscriberEmailRequest;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

import java.io.IOException;
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true)
public class NotificationConsumer {

    NotificationService notificationService;


    @RabbitListener(queues = "notification")
    public void consumer(SubscriberEmailRequest subscriberEmailRequest) throws IOException {
        log.info("Consumed {} from queue", subscriberEmailRequest.emailTo());
        notificationService.sendTextEmail(subscriberEmailRequest);
    }
}
