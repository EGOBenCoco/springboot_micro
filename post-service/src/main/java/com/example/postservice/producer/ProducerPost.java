package com.example.postservice.producer;

import com.example.plannerentity.dto.responce.CustomerResponce;
import com.example.plannerentity.dto.responce.PostEmailRecordDTO;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class ProducerPost {

    RabbitTemplate rabbitTemplate;

    public void publishEmailMessage(CustomerResponce consumer){

        var emailDTO = new PostEmailRecordDTO(
                consumer.getId(),
                consumer.getEmail(),
                "Wet Grapes",
                "Hello " + consumer.getNickname() + ". Your post added!");

        rabbitTemplate.convertAndSend("topic_exchange","for.post", emailDTO);
    }
}
