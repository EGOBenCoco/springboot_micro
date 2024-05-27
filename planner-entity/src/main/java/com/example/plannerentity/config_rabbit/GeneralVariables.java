package com.example.plannerentity.config_rabbit;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PUBLIC,makeFinal = true)
public class GeneralVariables {

     public static final String QUEUE_SUBSCRIBER = "subscriber";
     public static final String QUEUE_PROFILE = "profile";
     public static final String QUEUE_NOTIFICATION = "notification";
     public static final String QUEUE_POST = "post";
     public static final String EXCHANGE = "topic_exchange";
     public static final String NOTIFICATION_KEY = "for.notification";
     public static final String SUBSCRIBER_KEY = "for.subscriber";
     public static final String PROFILE_KEY = "for.profile";
     public static final String POST_KEY = "for.post";


     /*For DeadLetter*/

     public static final String EXCHANGE_DL = "topic_exchange_dl";
     public static final String QUEUE_SUBSCRIBER_DL = "subscriber.dl";
     public static final String QUEUE_PROFILE_DL = "profile.dl";
     public static final String QUEUE_NOTIFICATION_DL = "notification.dl";
     public static final String QUEUE_POST_DL = "post.dl";
     public static final String NOTIFICATION_KEY_DL = "for.notification.dl";
     public static final String SUBSCRIBER_KEY_DL = "for.subscriber.dl";
     public static final String PROFILE_KEY_DL = "for.profile.dl";
     public static final String POST_KEY_DL = "for.post.dl";
}
