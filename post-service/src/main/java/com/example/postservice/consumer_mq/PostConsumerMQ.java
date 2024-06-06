package com.example.postservice.consumer_mq;

import com.example.plannerentity.config_rabbit.GeneralVariables;
import com.example.plannerentity.dto.request.ProfileUpdateRequest;
import com.example.plannerentity.global_exception.ListenerException;
import com.example.postservice.model.Post;
import com.example.postservice.repository.PostRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@Transactional
public class PostConsumerMQ {
    PostRepository postRepository;
    @RabbitListener(queues = GeneralVariables.QUEUE_POST)
    public void updateNickName(Map<String, Object> message) throws ListenerException {
        int profileId = (int) message.get("profileId");
        String nickname = (String) message.get("nickname");
        List<Post> posts = postRepository.findAllByAccountId(profileId);
        if (posts.isEmpty()) {
            throw new ListenerException("Profile empty ");
        }
        posts.forEach(post -> post.setAuthor(nickname));
        postRepository.saveAll(posts);
        }


    }

