package com.example.notificationservice.service;


import com.example.plannerentity.dto.responce.SubscriberEmailRequest;
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
public class NotificationService {

    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);

    public String sendTextEmail(SubscriberEmailRequest sender) throws IOException {
        Email from = new Email("kkasanov92@gmail.com");
        String subject = sender.subject();
        Email to = new Email(sender.emailTo());
        Content content = new Content("text/plain", sender.text());
        Mail mail = new Mail(from, subject, to, content);

        SendGrid sg = new SendGrid("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");
        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sg.api(request);
            logger.info(response.getBody());
            return response.getBody();
        } catch (IOException ex) {
            throw ex;
        }

    }
}
