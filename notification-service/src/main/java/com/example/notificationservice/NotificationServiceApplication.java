package com.example.notificationservice;

import com.example.notificationservice.service.NotificationService;
import com.example.plannerentity.dto.responce.PostEmailRecordDTO;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

import java.io.IOException;

@SpringBootApplication
@EnableDiscoveryClient
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true)
@ComponentScan(basePackages = {"com.example.plannerentity","com.example.notificationservice"})
//@EnableJpaRepositories(basePackages = {"com.example.notificationservice"})
//@EntityScan(basePackages = {"com.example.plannerentity.notif"})
@Slf4j
public class NotificationServiceApplication {
      NotificationService notificationService;


    public static void main(String[] args) {
        SpringApplication.run(NotificationServiceApplication.class, args);
    }

    @RabbitListener(queues = "post")
    public void consumer(PostEmailRecordDTO postEmailRecordDTO) throws IOException {
        log.info("Consumed {} from queue", postEmailRecordDTO.emailTo());
        notificationService.sendTextEmail(postEmailRecordDTO);
    }



}
