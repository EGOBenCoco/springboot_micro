package com.example.postservice;

import com.example.postservice.config.OAuth2FeignRequestInterceptor;
import org.modelmapper.ModelMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = {"com.example.plannerentity","com.example.postservice"})
@EnableJpaRepositories(basePackages = {"com.example.postservice"})
//@EntityScan(basePackages = {"com.example.plannerentity.models.post"})
@EnableFeignClients
@EnableDiscoveryClient
public class PostServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(PostServiceApplication.class, args);

        String ulr = "https://s3.eu-north-1.amazonaws.com/benedigto-bucket/post/dd6718ce-24e1-4bc4-bccd-32410c47eab8/Снимок.PNG";
        String fileName = ulr.substring(ulr.lastIndexOf("/") + 1);
        System.out.println(fileName);
    }
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public OAuth2FeignRequestInterceptor oAuth2FeignRequestInterceptor() {
        return new OAuth2FeignRequestInterceptor();
    }
}
