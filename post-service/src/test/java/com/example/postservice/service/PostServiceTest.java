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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.keycloak.admin.client.Keycloak;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {
    @Mock
    private PostRepository postRepository;
    @Mock
    private ModelMapper modelMapper;
    @Mock
    private PostProducer postProducer;
    @Mock
    private ProfileClient profileClient;

    @InjectMocks
    private PostService postService;



    private Post post;
    @BeforeEach
    public void setUp(){
        post = Post.builder()
                .id(1)
                .name("Test Post")
                .content("Example")
                .category(Category.IT)
                .build();
    }

    @Test
    public void testFindAll() {
        // Given
        Page<Post> page = new PageImpl<>(Collections.singletonList(post));
        when(postRepository.findAllPost(any(PageRequest.class))).thenReturn(page);
        when(modelMapper.map(page, new TypeToken<Page<PostResponce>>() {}.getType())).thenReturn(new PageImpl<>(Collections.singletonList(new PostResponce())));

        // When
        Page<PostResponce> result = postService.findAll(0, 10);

        // Then
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(page.getTotalElements(), result.getTotalElements());

        // Add more assertions as needed
    }

    @Test
    public void testGetById() {
        // Given
        when(postRepository.findById(1)).thenReturn(Optional.of(post));

        // When
        Post result = postService.getById(1);

        // Then
        assertNotNull(result);
        assertEquals("Test Post", result.getName());
        // Add more assertions as needed
    }


    @Test
    public void testCreatePost() {
        // Arrange


        ProfileResponce profileResponse = new ProfileResponce();
        profileResponse.setId(1);
        profileResponse.setNickname("TestUser");

        when(profileClient.getById(post.getAccountId())).thenReturn(profileResponse);

        // Act
        postService.createPost(post);

        // Assert
        verify(postRepository, times(1)).save(post);

        PostSubscriberMessage expectedMessage = new PostSubscriberMessage();
        expectedMessage.setProfileId(1);
        expectedMessage.setPostTitle("Test Post");
        expectedMessage.setAuthorName("TestUser");

        verify(postProducer, times(1)).sendPostDataToRabbitMQ(expectedMessage);
    }


    @Test
    void updatePost_PostFound_UpdateSuccessful() {
        // Arrange
        int postId = 1;
        PostUpdateRequest updateRequest = new PostUpdateRequest();
        updateRequest.setName("New Name");
        updateRequest.setContent("New Content");
        post.setId(postId);
        when(postRepository.findById(postId)).thenReturn(Optional.of(post));

        // Act
        postService.updatePost(postId, updateRequest);

        // Assert
        verify(postRepository).findById(postId);
        verify(postRepository).save(any(Post.class));
    }
    @Test
    void updatePost_PostNotFound_CustomExceptionThrown() {
        // Arrange
        int postId = 1;
        PostUpdateRequest updateRequest = new PostUpdateRequest();
        when(postRepository.findById(postId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(CustomException.class, () -> postService.updatePost(postId, updateRequest));
    }

    @Test
    void deleteById_PostExists_DeletionSuccessful() {
        // Arrange
        int postId = 1;

        // Act
        postService.deleteById(postId);

        // Assert
        verify(postRepository).deleteById(postId);
    }
}