package com.example.plannerentity.config_rabbit;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DeadLetterConfig extends GeneralVariables {

    @Bean
    TopicExchange deadLetterExchange() {
        return new TopicExchange(EXCHANGE_DL);
    }

    @Bean
    Queue dlq_post() {
        return QueueBuilder.durable(QUEUE_POST_DL).build();
    }

    @Bean
    Queue dlq_subscriber() {
        return QueueBuilder.durable(QUEUE_SUBSCRIBER_DL).build();
    }

    @Bean
    Queue dlq_profile() {
        return QueueBuilder.durable(QUEUE_PROFILE_DL).build();
    }

    @Bean
    Queue dlq_notification() {
        return QueueBuilder.durable(QUEUE_NOTIFICATION_DL).build();
    }

    @Bean
    public Declarables topicExchangeBindings_dl(
            TopicExchange deadLetterExchange,
            Queue dlq_post,
            Queue dlq_subscriber,
            Queue dlq_profile,
            Queue dlq_notification) {
        return new Declarables(BindingBuilder.bind(dlq_post).to(deadLetterExchange).with(POST_KEY_DL),
                BindingBuilder.bind(dlq_subscriber).to(deadLetterExchange).with(SUBSCRIBER_KEY_DL),
                BindingBuilder.bind(dlq_profile).to(deadLetterExchange).with(PROFILE_KEY_DL),
                BindingBuilder.bind(dlq_notification).to(deadLetterExchange).with(NOTIFICATION_KEY_DL)
        );
    }

    @Bean
    public MessageConverter messageConverter_dl() {
        return new Jackson2JsonMessageConverter();
    }

}
