package com.example.plannerentity.config_rabbit;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.AsyncRabbitTemplate;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String QUEUE_SUBSCRIBER ="subscriber";
    public static final String QUEUE_PROFILE ="profile";
    public static final String QUEUE_NOTIFICATION ="notification";
    public static final String EXCHANGE = "topic_exchange";

    public static final String NOTIFICATION_KEY = "for.notification";
    public static final String SUBSCRIBER_KEY = "for.subscriber";
    public static final String PROFILE_KEY = "for.profile";

    @Bean
    public Queue queue_subscriber() {return new Queue(QUEUE_SUBSCRIBER);}

    @Bean
    public Queue queue_notification() {return new Queue(QUEUE_NOTIFICATION);}
    @Bean
    public Queue queue_profile() {return new Queue(QUEUE_PROFILE);}


    @Bean
    public TopicExchange directExchange() {
        return new TopicExchange(EXCHANGE);
    }

 /*   @Bean
    public AsyncRabbitTemplate asyncRabbitTemplate(RabbitTemplate rabbitTemplate){
        return new AsyncRabbitTemplate(rabbitTemplate);
    }*/

    @Bean
    public Binding binder1() {
        return BindingBuilder
                .bind(queue_subscriber())
                .to(directExchange())
                .with(SUBSCRIBER_KEY);
    }

    @Bean
    public Binding binder2() {
        return BindingBuilder
                .bind(queue_notification())
                .to(directExchange())
                .with(NOTIFICATION_KEY);
    }

    @Bean
    public Binding binder3() {
        return BindingBuilder
                .bind(queue_profile())
                .to(directExchange())
                .with(PROFILE_KEY);
    }



    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public AmqpTemplate template(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter());
        return template;
    }
}
