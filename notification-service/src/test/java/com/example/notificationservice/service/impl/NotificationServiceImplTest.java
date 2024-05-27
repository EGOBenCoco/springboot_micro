package com.example.notificationservice.service.impl;

import com.example.plannerentity.dto.request.SubscriberEmailRequest;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;import org.slf4j.Logger;

@ExtendWith(MockitoExtension.class)
/*
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
@ExtendWith(SpringExtension.class)
@SpringBootTest(properties = {"spring.config.location=classpath:application-test.properties"},webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations ="classpath:application-test.properties",properties = {"eureka.client.enabled=false"} )
*/

class NotificationServiceImplTest {

    @Mock
    SendGrid sendGrid;

    @InjectMocks
    NotificationServiceImpl notificationService;

    @Test
    void sendTextEmail() throws IOException {
        SubscriberEmailRequest sender = new SubscriberEmailRequest("test@example.com", "Test Subject", "Test Message");

        Response mockedResponse = new Response();
        mockedResponse.setStatusCode(202);
        when(sendGrid.api(any(Request.class))).thenReturn(mockedResponse);

        notificationService.sendTextEmail(sender);

        verify(sendGrid, times(1)).api(any(Request.class));
    }
    @Test
    void sendTextEmail_Error() throws IOException {
        SubscriberEmailRequest sender = new SubscriberEmailRequest("test@example.com", "Test Subject", "Test Message");

        when(sendGrid.api(any(Request.class))).thenThrow(new IOException("Simulated IOException"));

        assertThrows(RuntimeException.class, () -> notificationService.sendTextEmail(sender));
    }
}