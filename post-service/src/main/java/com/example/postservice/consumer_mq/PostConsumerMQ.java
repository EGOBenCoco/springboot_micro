package com.example.postservice.consumer_mq;

import com.example.plannerentity.config_rabbit.GeneralVariables;
import com.example.plannerentity.dto.request.ProfileUpdateRequest;
import com.example.plannerentity.global_exception.ListenerException;
import com.example.postservice.model.Post;
import com.example.postservice.repository.PostRepository;
import com.example.postservice.service.PostService;
import com.example.postservice.service.impl.PostServiceImpl;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class PostConsumerMQ {
    PostRepository postRepository;
    @RabbitListener(queues = GeneralVariables.QUEUE_POST)
    public void updateNickName(ProfileUpdateRequest profileUpdateRequest) throws ListenerException {
        List<Post> posts = postRepository.findAllByAccountId(profileUpdateRequest.id());
        if (posts.isEmpty()) {
            throw new ListenerException("Profile empty ");
        }
        posts.forEach(post -> post.setNickname(profileUpdateRequest.nickname()));
        postRepository.saveAll(posts);
        };
    }

