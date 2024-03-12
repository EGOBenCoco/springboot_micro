package com.example.postservice.controller;

import com.example.plannerentity.dto.request.PostUpdateRequest;
import com.example.plannerentity.dto.responce.PostResponce;
import com.example.plannerentity.enums.Category;
import com.example.postservice.model.Post;
import com.example.postservice.service.PostService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class PostController {

    PostService postService;

    @GetMapping("/{id}")
    public ResponseEntity<Post> getById(@PathVariable int id){
        return ResponseEntity.ok(postService.getById(id));

    }
    @GetMapping("/admin")
    public ResponseEntity<String> getAdmin(Principal principal) {
        JwtAuthenticationToken token = (JwtAuthenticationToken) principal;
        String userName = (String) token.getTokenAttributes().get("name");
        String userEmail = (String) token.getTokenAttributes().get("email");
        return ResponseEntity.ok("Hello Admin \nUser Name : " + userName + "\nUser Email : " + userEmail);
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<UserRepresentation> searchByUsername(@PathVariable String username) {
        return ResponseEntity.ok().body(postService.searchByUsername(username));
    }

    @GetMapping("/all")
    public ResponseEntity<Page<PostResponce>> findAll(@RequestParam(name = "page", defaultValue = "0") int page,
                                                      @RequestParam(name = "size", defaultValue = "10") int size){
        return ResponseEntity.ok(postService.findAll(page,size));
    }

    @GetMapping("/{nickname}/consumer")
    public ResponseEntity<Post> getByConsumerId(@PathVariable String nickname){
        return ResponseEntity.ok(postService.getByConsumerId(nickname));
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> update(@PathVariable int id, @RequestBody PostUpdateRequest updateRequest){
        postService.updatePost(id,updateRequest);
        return ResponseEntity.ok("Your post update");
    }

    @PostMapping
    public ResponseEntity<String> create(@RequestBody Post post, Principal principal){
        postService.createPost(post,principal);
        return ResponseEntity.ok("Create Post");
    }

    @GetMapping("/sort")
    public ResponseEntity<Page<PostResponce>> getByCategory(@RequestParam("category")Category category,
                                                            @RequestParam(name = "page", defaultValue = "0") int page,
                                                            @RequestParam(name = "size", defaultValue = "10") int size){
        return ResponseEntity.ok(postService.findByCategory(category,page,size));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable int id){
        postService.deleteById(id);
        return ResponseEntity.ok("Delete");
    }
}
