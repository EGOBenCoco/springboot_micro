package com.example.postservice.controller;

import com.example.plannerentity.dto.request.PostCreatedRequest;
import com.example.plannerentity.dto.request.PostUpdateRequest;
import com.example.plannerentity.dto.responce.PostResponce;
import com.example.plannerentity.enums.Category;
import com.example.plannerentity.global_exception.SuccessMessage;
import com.example.postservice.model.Post;
import com.example.postservice.service.PostService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PostController {

    PostService postService;

    @PostMapping("/{post-id}/add-photo")
    public ResponseEntity<Object> addPhotosToEntityById(
            @PathVariable("post-id") int entityId,
            @RequestParam("files") List<MultipartFile> file
    ) {
        postService.addPhotoById(entityId, file);

        return ResponseEntity.ok(SuccessMessage.builder()
                .status(HttpStatus.CREATED.value())
                .message("Photo added")
                .datetime(LocalDateTime.now())
                .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Post> getById(@PathVariable int id) {
        return ResponseEntity.ok(postService.getById(id));
    }

    @GetMapping("/all")
    public ResponseEntity<Page<Post>> findAll(@RequestParam(name = "page", defaultValue = "0") int page,
                                                      @RequestParam(name = "size", defaultValue = "10") int size) {
        return ResponseEntity.ok(postService.findAll(page, size));
    }

    @GetMapping("/{nickname}/profile")
    public ResponseEntity<Page<Post>> getByConsumerId(@PathVariable String nickname,
                                                              @RequestParam(name = "page", defaultValue = "0") int page,
                                                              @RequestParam(name = "size", defaultValue = "10") int size) {
        return ResponseEntity.ok(postService.getByNickname(nickname, page, size));
    }

    @GetMapping("/{name}/by-name")
    public ResponseEntity<Page<Post>> getByName(@PathVariable String name,
                                                        @RequestParam(name = "page", defaultValue = "0") int page,
                                                        @RequestParam(name = "size", defaultValue = "10") int size) {
        return ResponseEntity.ok(postService.getByNickname(name, page, size));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> update(@PathVariable int id, @RequestBody PostUpdateRequest updateRequest) {
        postService.updatePost(id, updateRequest);
        return ResponseEntity.ok(SuccessMessage.builder()
                .status(HttpStatus.OK.value())
                .message("Post updated")
                .datetime(LocalDateTime.now())
                .build());
    }

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody PostCreatedRequest post) {
        postService.createPost(post);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(SuccessMessage.builder()
                        .status(HttpStatus.CREATED.value())
                        .message("Post created")
                        .datetime(LocalDateTime.now())
                        .build());
    }

    @GetMapping("/sort")
    public ResponseEntity<Page<Post>> getByCategory(@RequestParam("category") Category category,
                                                            @RequestParam(name = "page", defaultValue = "0") int page,
                                                            @RequestParam(name = "size", defaultValue = "10") int size) {
        return ResponseEntity.ok(postService.findByCategory(category, page, size));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable int id) {
        postService.deleteById(id);
        return ResponseEntity.ok(SuccessMessage.builder()
                .status(HttpStatus.OK.value())
                .message("Post deleted")
                .datetime(LocalDateTime.now())
                .build());
    }

    @DeleteMapping("/{post-id}/delete-photo")
    public ResponseEntity<Object> deletePhotoFromEntityById(
            @PathVariable("post-id") int announcementId,
            @RequestParam("photo-url") String photoUrl
    ) {
        postService.deletePhotoById(announcementId, photoUrl);

        return ResponseEntity.ok(SuccessMessage.builder()
                .status(HttpStatus.OK.value())
                .message("Photo deleted")
                .datetime(LocalDateTime.now())
                .build());
    }
}
