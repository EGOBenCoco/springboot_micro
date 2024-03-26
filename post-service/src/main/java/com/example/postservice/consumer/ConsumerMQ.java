package com.example.postservice.consumer;

import com.example.plannerentity.dto.request.ProfileUpdateRequest;
import com.example.postservice.model.Post;
import com.example.postservice.repository.PostRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class ConsumerMQ {
    PostRepository postRepository;

    @RabbitListener(queues = "post")
    public void updateNickName(ProfileUpdateRequest profileUpdateRequest){
        List<Post> posts = postRepository.findAllByAccountId(profileUpdateRequest.getId());
        posts.forEach(post -> post.setNickname(profileUpdateRequest.getNickname()));
        postRepository.saveAll(posts);
        };
    }

