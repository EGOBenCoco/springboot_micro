package com.example.notificationservice.config;

import com.sendgrid.SendGrid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SendGridConfig {

    @Value("${SENDGRID_API_KEY}")
    private String apiKey;

    @Value("${SENDGRID_API_INTEGRATION_ENABLED}")
    private boolean isSendGridIntegrationEnabled = false;

    @Bean
    public SendGrid sendgrid() {
        if (isSendGridIntegrationEnabled && apiKey == null) {
            throw new RuntimeException("SendGrid is enabled but is missing an API key. Please set a sendgrid.apiKey property value.");
        }
        return new SendGrid(apiKey);
    }
}