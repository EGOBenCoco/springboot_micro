package com.example.plannerentity.dto.responce;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PostSubscriberMessage {
        int profileId;
        String postTitle;
        String authorName;
        String emailSubscriber;

        public PostSubscriberMessage(int profileId, String postTitle, String authorName) {
                this.profileId = profileId;
                this.postTitle = postTitle;
                this.authorName = authorName;
        }
}
