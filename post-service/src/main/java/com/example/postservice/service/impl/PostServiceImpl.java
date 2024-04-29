package com.example.postservice.service.impl;

import com.example.plannerentity.dto.request.PostUpdateRequest;
import com.example.plannerentity.dto.responce.PostResponce;
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
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.lang.reflect.Type;
import java.util.List;
import java.util.UUID;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    PostRepository postRepository;
    ProfileClient profileClient;
    ModelMapper modelMapper;
    PostProducerMQ postProducerMQ;
    S3FileService s3FileService;

    public void createPost(Post post) {
        ProfileResponce profile = profileClient.getById(post.getAccountId());
        if(profile.getAuth_id() == null){
            throw new CustomException("Something wrong with your profile. Check",HttpStatus.NOT_FOUND);
        }
        post.setAccountId(profile.getId());
        post.setNickname(profile.getNickname());
        postRepository.save(post);
        PostSubscriberMessage message = new PostSubscriberMessage
        (profile.getId(), post.getName(), post.getNickname());

        postProducerMQ.sendPostDataToRabbitMQ(message);
    }
    public Page<PostResponce> findAll(int page, int size ){
        Page<Post> postPage = postRepository.findAllPost(PageRequest.of(page,size));
        if(postPage.isEmpty()){
            throw new CustomException("Posts not found", HttpStatus.NOT_FOUND);
        }
        Type postResponceListType = new TypeToken<Page<PostResponce>>() {}.getType();
        return modelMapper.map(postPage,postResponceListType);
    }

    public Page<PostResponce> getByNickname(String nickname,int page,int size){
        Page<Post> postPage = postRepository.findAllByNickname(nickname, PageRequest.of(page,size));
        if(postPage.isEmpty()){
            throw new CustomException("Posts not found", HttpStatus.NOT_FOUND);
        }
        Type postResponceListType = new TypeToken<Page<PostResponce>>() {}.getType();
        return modelMapper.map(postPage,postResponceListType);
    }

    @Override
    public Page<PostResponce> getByName(String name,int page,int size) {
        Page<Post> postPage = postRepository.findAllByName(name, PageRequest.of(page,size));
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


    public void updatePost(int postId, PostUpdateRequest updateRequest){
        postRepository.findById(postId).ifPresentOrElse(post ->
        {
            post.setName(updateRequest.name());
            post.setContent(updateRequest.content());
            post.setCategory(updateRequest.category());
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

    @Override
    public void addPhotoById(int announcementId, List<MultipartFile> files) {
        Post announcement = postRepository.findById(announcementId)
                .orElseThrow(() -> new CustomException("Post not found", HttpStatus.NOT_FOUND));
        List<String> newPhotoUrls = files.stream()
                .filter(file -> file != null && !file.isEmpty())
                .map(s3FileService::uploadFile)
                .toList();
        announcement.getPhotoUrls().addAll(newPhotoUrls);
        postRepository.save(announcement);
    }
    public void deleteById(int id){
        postRepository.deleteById(id);
    }
}
