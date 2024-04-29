package com.example.plannerentity.config_rabbit;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig extends GeneralVariables {
    @Bean
    public Queue queue_subscriber() {return  QueueBuilder
            .durable(QUEUE_SUBSCRIBER).withArgument("x-dead-letter-exchange",EXCHANGE_DL)
            .withArgument("x-dead-letter-routing-key", SUBSCRIBER_KEY_DL)
            .build();}
    @Bean
    public Queue queue_post() {return  QueueBuilder
            .durable(QUEUE_POST).withArgument("x-dead-letter-exchange",EXCHANGE_DL)
            .withArgument("x-dead-letter-routing-key", POST_KEY_DL)
            .build();}
    @Bean
    public Queue queue_notification() {return  QueueBuilder
            .durable(QUEUE_NOTIFICATION).withArgument("x-dead-letter-exchange",EXCHANGE_DL)
            .withArgument("x-dead-letter-routing-key", NOTIFICATION_KEY_DL)
            .build();}

    @Bean
    public Queue queue_profile() {return  QueueBuilder
            .durable(QUEUE_PROFILE).withArgument("x-dead-letter-exchange",EXCHANGE_DL)
            .withArgument("x-dead-letter-routing-key", PROFILE_KEY_DL)
            .build();}

    @Bean
    public TopicExchange directExchange() {
        return new TopicExchange(EXCHANGE);
    }
    @Bean
    public Declarables topicExchangeBindings(
            TopicExchange directExchange,
            Queue queue_post,
            Queue queue_subscriber,
            Queue queue_profile,
            Queue queue_notification){
        return new Declarables(BindingBuilder.bind(queue_post).to(directExchange).with(POST_KEY),
                BindingBuilder.bind(queue_subscriber).to(directExchange).with(SUBSCRIBER_KEY),
                BindingBuilder.bind(queue_profile).to(directExchange).with(PROFILE_KEY),
                BindingBuilder.bind(queue_notification).to(directExchange).with(NOTIFICATION_KEY)
        );
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
