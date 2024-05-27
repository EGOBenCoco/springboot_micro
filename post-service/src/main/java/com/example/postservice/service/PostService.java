package com.example.postservice.service;

import com.example.plannerentity.dto.request.PostCreatedRequest;
import com.example.plannerentity.dto.request.PostUpdateRequest;
import com.example.plannerentity.dto.responce.PostResponce;
import com.example.plannerentity.enums.Category;
import com.example.postservice.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;

public interface PostService {
    Page<Post> findAll(int page, int size);
    Page<Post> getByNickname(String nickname, int page, int size);
    Page<Post> getByName(String name, int page, int size);
    Post getById(int id);
    Page<Post> findByCategory(Category category, int page, int size);
    void createPost(PostCreatedRequest post);
    void updatePost(int postId, PostUpdateRequest updateRequest);
    void deleteById(int id);
    void addPhotoById(int postId, List<MultipartFile> files);
    void deletePhotoById(int postId, String photoUrl);
}
