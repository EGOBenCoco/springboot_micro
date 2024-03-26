package com.example.postservice.service;

import com.example.plannerentity.dto.request.PostUpdateRequest;
import com.example.plannerentity.dto.responce.PostResponce;
import com.example.plannerentity.dto.responce.PostSubscriberMessage;
import com.example.plannerentity.dto.responce.ProfileResponce;
import com.example.plannerentity.enums.Category;
import com.example.plannerentity.global_exception.CustomException;
import com.example.postservice.client.ProfileClient;
import com.example.postservice.model.Post;
import com.example.postservice.producer.PostProducer;
import com.example.postservice.repository.PostRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@RequiredArgsConstructor
public class PostService {

    PostRepository postRepository;
    ProfileClient profileClient;
    ModelMapper modelMapper;
    PostProducer postProducer;
    public Page<PostResponce> findAll(int page, int size ){
        Page<Post> postPage = postRepository.findAllPost(PageRequest.of(page,size));
        if(postPage.isEmpty()){
            throw new CustomException("Posts not found", HttpStatus.NOT_FOUND);
        }
        Type postResponceListType = new TypeToken<Page<PostResponce>>() {}.getType();
        return modelMapper.map(postPage,postResponceListType);
    }

    public Page<PostResponce> getByNickname(String nickname,int page,int size){
        Page<Post> postPage = postRepository.findByNickname(nickname, PageRequest.of(page,size));
        if(postPage.isEmpty()){
            throw new CustomException("Posts not found", HttpStatus.NOT_FOUND);
        }
        Type postResponceListType = new TypeToken<Page<PostResponce>>() {}.getType();
        return modelMapper.map(postPage,postResponceListType);
    }
    public Page<PostResponce> getByAccountId(int accountId,int page,int size){
        Page<Post> postPage = postRepository.findAllByAccountId(accountId, PageRequest.of(page,size));
        if(postPage.isEmpty()){
            throw new CustomException("Posts not found", HttpStatus.NOT_FOUND);
        }
        Type postResponceListType = new TypeToken<Page<PostResponce>>() {}.getType();
        return modelMapper.map(postPage,postResponceListType);
    }

    public Post getById(int id) {
        return postRepository.findById(id).orElseThrow(()-> new CustomException("Post not found", HttpStatus.NOT_FOUND));
    }

    public Page<PostResponce> findByCategory(Category category, int page, int size){
        Page<Post> postPage = postRepository.findByCategory(category, PageRequest.of(page,size));
        if(postPage.isEmpty()){
            throw new CustomException("Posts not found", HttpStatus.NOT_FOUND);
        }
        Type postResponceListType = new TypeToken<Page<PostResponce>>() {}.getType();
        return modelMapper.map(postPage,postResponceListType);
    }
    public void createPost(Post post) {
        ProfileResponce profile = profileClient.getById(post.getAccountId());
        post.setAccountId(profile.getId());
        post.setNickname(profile.getNickname());
        postRepository.save(post);
        PostSubscriberMessage message = new PostSubscriberMessage();
        message.setProfileId(profile.getId());
        message.setPostTitle(post.getName());
        message.setAuthorName(post.getNickname());
        postProducer.sendPostDataToRabbitMQ(message);
    }

    public void updatePost(int postId, PostUpdateRequest updateRequest){
        postRepository.findById(postId).ifPresentOrElse(post ->
        {
            post.setName(updateRequest.getName());
            post.setContent(updateRequest.getContent());
            post.setCategory(updateRequest.getCategory());
            postRepository.save(post);
        }, () -> {
            throw new CustomException("Post not found",HttpStatus.NOT_FOUND);
        });
    }


    public void deleteById(int id){
        postRepository.deleteById(id);
    }
}
