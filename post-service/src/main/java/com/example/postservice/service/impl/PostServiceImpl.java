package com.example.postservice.service.impl;

import com.example.plannerentity.dto.request.PostCreatedRequest;
import com.example.plannerentity.dto.request.PostUpdateRequest;
import com.example.plannerentity.dto.responce.PostSubscriberMessage;
import com.example.plannerentity.dto.responce.ProfileResponce;
import com.example.plannerentity.enums.Category;
import com.example.plannerentity.global_exception.CustomException;
import com.example.postservice.client.ProfileClient;
import com.example.postservice.model.Post;
import com.example.postservice.producer_mq.PostProducerMQ;
import com.example.postservice.repository.PostRepository;
import com.example.postservice.service.PostService;
import com.example.postservice.service.S3FileService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@RequiredArgsConstructor
@Transactional
public class PostServiceImpl implements PostService {

    PostRepository postRepository;
    ProfileClient profileClient;
    ModelMapper modelMapper;
    PostProducerMQ postProducerMQ;
    S3FileService s3FileService;
    @Override
    public void addPhotoById(int announcementId, List<MultipartFile> files) {
        Post post = postRepository.findById(announcementId)
                .orElseThrow(() -> new CustomException("Post not found", HttpStatus.NOT_FOUND));
        List<String> newPhotoUrls = files.stream()
                .filter(file -> file != null && !file.isEmpty())
                .map(s3FileService::uploadFile)
                .toList();
        post.getPhotoUrls().addAll(newPhotoUrls);
        postRepository.save(post);
    }
    public void createPost(PostCreatedRequest createdRequest) {
        String nickname = getTokenAttribute("preferred_username");
        ProfileResponce profile = profileClient.getByNickname(nickname);

        Post post = modelMapper.map(createdRequest,Post.class);
        post.setProfileId(profile.getId());
        post.setAuthor(profile.getNickname());
        postRepository.save(post);
        PostSubscriberMessage message = new PostSubscriberMessage
        (profile.getId(), createdRequest.getName(), post.getAuthor());
        postProducerMQ.sendPostDataToRabbitMQ(message);
    }


    public Page<Post> findAll(int page, int size ){
        Page<Post> postPage = postRepository.findAllPost(PageRequest.of(page,size));
        if(postPage.isEmpty()){
            throw new CustomException("Posts not found", HttpStatus.NOT_FOUND);
        }
        return postPage;
    }

    public Page<Post> getByNickname(String nickname,int page,int size){
        Page<Post> postPage = postRepository.findAllByAuthor(nickname, PageRequest.of(page,size));
        if(postPage.isEmpty()){
            throw new CustomException("Posts not found", HttpStatus.NOT_FOUND);
        }
        return postPage;
    }

    @Override
    public Page<Post> getByName(String name,int page,int size) {
        Page<Post> postPage = postRepository.findAllByName(name, PageRequest.of(page,size));
        if(postPage.isEmpty()){
            throw new CustomException("Posts not found", HttpStatus.NOT_FOUND);
        }
        return postPage;
    }
    public Post getById(int id) {
        return postRepository.findById(id).orElseThrow(()-> new CustomException("Post not found", HttpStatus.NOT_FOUND));
    }

    public Page<Post> findByCategory(Category category, int page, int size){
        Page<Post> postPage = postRepository.findByCategory(category, PageRequest.of(page,size));
        if(postPage.isEmpty()){
            throw new CustomException("Posts not found", HttpStatus.NOT_FOUND);
        }
        return postPage;
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

    @Override
    public void deletePhotoById(int postId, String photoUrl) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException("Post not found", HttpStatus.NOT_FOUND));
        List<String> photoUrls = post.getPhotoUrls();
        if (photoUrls.contains(photoUrl)) {
            s3FileService.deleteFileFromS3Bucket(photoUrl);
            photoUrls.remove(photoUrl);
            postRepository.save(post);
        }
        else{
            throw new CustomException("Photo not found for the given post", HttpStatus.NOT_FOUND);
        }
    }
    public void deleteById(int id){
        postRepository.deleteById(id);
    }


    private String getTokenAttribute(String arg) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        JwtAuthenticationToken token = (JwtAuthenticationToken) authentication;
        return (String) token.getTokenAttributes().get(arg);
    }
}
