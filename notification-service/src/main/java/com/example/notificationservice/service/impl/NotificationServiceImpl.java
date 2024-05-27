package com.example.notificationservice.service.impl;


import com.example.notificationservice.service.NotificationService;
import com.example.plannerentity.dto.request.SubscriberEmailRequest;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true)
public class NotificationServiceImpl implements NotificationService {

    SendGrid sendGrid;
    private static final Logger logger = LoggerFactory.getLogger(NotificationServiceImpl.class);

    public void sendTextEmail(SubscriberEmailRequest sender){
        Email from = new Email("kkasanov92@gmail.com");
        String subject = sender.subject();
        Email to = new Email(sender.emailTo());
        Content content = new Content("text/plain", sender.text());
        Mail mail = new Mail(from, subject, to, content);
        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sendGrid.api(request);
            logger.info(response.getBody());
            response.getBody();
        } catch (IOException ex) {
            logger.error("Error sending email: {}", ex.getMessage(), ex);
            throw new RuntimeException("Error sending email", ex);
        }

    }
}
