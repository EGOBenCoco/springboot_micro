package com.example.postservice.service;

import com.example.plannerentity.dto.request.PostUpdateRequest;
import com.example.plannerentity.dto.responce.PostResponce;
import com.example.plannerentity.enums.Category;
import com.example.postservice.client.CustomerClient;
import com.example.postservice.model.Post;
import com.example.postservice.producer.ProducerPost;
import com.example.postservice.repository.PostRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.UserRepresentation;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.security.Principal;
import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@RequiredArgsConstructor
public class PostService {

    PostRepository postRepository;
    CustomerClient customerClient;
    ProducerPost producerPost;
    ModelMapper modelMapper;

    Keycloak keycloak;


    public UserRepresentation searchByUsername(String username){
        List<UserRepresentation> userRepresentations = keycloak.realm("master").users()
                .search(username, true);
        return userRepresentations.get(0);
    }
    public Page<PostResponce> findAll(int page, int size ){
        Page<Post> postPage = postRepository.findAllPost(PageRequest.of(page,size));
        Type postResponceListType = new TypeToken<Page<PostResponce>>() {}.getType();
        return modelMapper.map(postPage,postResponceListType);

    }
    public Post getByConsumerId(String nickname){
        return postRepository.findByNickname(nickname);
    }
    public Post getById(int id) {
        return postRepository.findById(id).orElseThrow();
    }
    public Page<PostResponce> findByCategory(Category category, int page, int size){
        Page<Post> postPage = postRepository.findByCategory(category, PageRequest.of(page,size));
        Type postResponceListType = new TypeToken<Page<PostResponce>>() {}.getType();
        return modelMapper.map(postPage,postResponceListType);
    }
    public void createPost(Post post, Principal principal){
        JwtAuthenticationToken token = (JwtAuthenticationToken) principal;
        String userName = (String) token.getTokenAttributes().get("name");

        post.setNickname(userName);
        postRepository.save(post);
   /*     UserRepresentation users =(UserRepresentation) keycloak.realm("master").users()
                .search(post.getNickname(), true);
        post.setNickname(users.toString());
        postRepository.save(post);*/


/*        CustomerResponce customer = customerClient.getById(post.getCustomerId());
        post.setCustomerId(customer.getId());
        post.setNickname(customer.getNickname());
        postRepository.save(post);
        producerPost.publishEmailMessage(customer);*/




    }
    public void updatePost(int postId, PostUpdateRequest updateRequest){
        postRepository.findById(postId).ifPresentOrElse(post ->
        {
            post.setName(updateRequest.getName());
            post.setContent(updateRequest.getContent());
            post.setCategory(updateRequest.getCategory());
            postRepository.save(post);
        }, () -> {
            //throw new CustomException("Consumer not found",HttpStatus.NOT_FOUND);
        });
    }

    public void deleteById(int id){
        postRepository.deleteById(id);
    }
}
