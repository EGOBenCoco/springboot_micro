package com.example.plannerentity.dto.responce;

import lombok.Data;

@Data
public class PostSubscriberMessage {
        int profileId;
        String postTitle;
        String authorName;
        String emailSubscriber;
}
