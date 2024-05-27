package com.example.plannerentity.dto.responce;

import lombok.Data;

@Data
public class SubscriberResponce {
    int subscriberId;
    int profileId;
    String profileName;

    public SubscriberResponce(int subscriberId, int profileId, String profileName) {
        this.subscriberId = subscriberId;
        this.profileId = profileId;
        this.profileName = profileName;
    }
}
