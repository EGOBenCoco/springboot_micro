package com.example.subscriberservice;


import com.example.subscriberservice.client.OAuth2FeignRequestInterceptor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = {"com.example.plannerentity","com.example.subscriberservice"})
@EnableJpaRepositories(basePackages = {"com.example.subscriberservice"})
@EnableFeignClients
@EnableDiscoveryClient
public class SubcscriberServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(SubcscriberServiceApplication.class, args);
	}
	@Bean
	public OAuth2FeignRequestInterceptor oAuth2FeignRequestInterceptor() {
		return new OAuth2FeignRequestInterceptor();
	}
}
