package com.example.notificationservice.service;

import com.example.plannerentity.dto.request.SubscriberEmailRequest;

import java.io.IOException;

public interface NotificationService {
    void sendTextEmail(SubscriberEmailRequest sender)throws IOException;
}
