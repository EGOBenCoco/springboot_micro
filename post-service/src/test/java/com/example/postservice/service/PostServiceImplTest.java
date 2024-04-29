/*
package com.example.postservice.service;

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
import com.example.postservice.service.impl.PostServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PostServiceImplTest {
    @Mock
    private PostRepository postRepository;
    @Mock
    private ModelMapper modelMapper;
    @Mock
    private PostProducerMQ postProducerMQ;
    @Mock
    private ProfileClient profileClient;

    @InjectMocks
    private PostServiceImpl postServiceImpl;



    private Post post;
    @BeforeEach
    public void setUp(){
        post = Post.builder()
                .id(1)
                .name("testNickname")
                .content("Example")
                .category(Category.IT)
                .build();
    }
    @Test
    public void testGetByAccountId() {
        // Arrange
        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<Post> postPage = new PageImpl<>(Collections.singletonList(post), pageRequest, 1);
        when(postRepository.findAllByAccountId(123, pageRequest)).thenReturn(postPage);

        Type postResponceListType = new TypeToken<Page<PostResponce>>() {}.getType();
        when(modelMapper.map(postPage, postResponceListType)).thenReturn(new PageImpl<>(Collections.singletonList(new PostResponce()), pageRequest, 1));

        // Act
        Page<PostResponce> result = postServiceImpl.getByAccountId(123, 0, 10);

        // Assert
        assertNotNull(result);
        // Add your specific assertions here based on the expected behavior of getByAccountId
    }

    @Test
    public void testGetByAccountIdWhenNoPostsFound() {
        // Arrange
        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<Post> emptyPostPage = new PageImpl<>(Collections.emptyList(), pageRequest, 0);
        when(postRepository.findAllByAccountId(456, pageRequest)).thenReturn(emptyPostPage);

        // Act and Assert
        CustomException exception = assertThrows(CustomException.class, () -> {
            postServiceImpl.getByAccountId(456, 0, 10);
        });

        assertEquals("Posts not found", exception.getMessage());
    }
    @Test
    public void testGetByNicknameWhenNoPostsFound() {
        // Arrange
        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<Post> emptyPostPage = new PageImpl<>(Collections.emptyList(), pageRequest, 0);
        when(postRepository.findAllByNickname("nonExistentNickname", pageRequest)).thenReturn(emptyPostPage);

        // Act and Assert
         assertThrows(CustomException.class, () -> {
            postServiceImpl.getByNickname("nonExistentNickname", 0, 10);
        });

    }
    @Test
    public void testGetByNickname() {
        // Arrange
        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<Post> postPage = new PageImpl<>(Collections.singletonList(post), pageRequest, 1);
        when(postRepository.findAllByNickname("testNickname", pageRequest)).thenReturn(postPage);

        Type postResponceListType = new TypeToken<Page<PostResponce>>() {}.getType();
        when(modelMapper.map(postPage, postResponceListType)).thenReturn(new PageImpl<>(Collections.singletonList(new PostResponce()), pageRequest, 1));

        // Act
        Page<PostResponce> result = postServiceImpl.getByNickname("testNickname", 0, 10);

        // Assert
        assertNotNull(result);
    }

    @Test
    public void testFindAll() {
        // Given
        Page<Post> page = new PageImpl<>(Collections.singletonList(post));
        when(postRepository.findAllPost(any(PageRequest.class))).thenReturn(page);
        when(modelMapper.map(page, new TypeToken<Page<PostResponce>>() {}.getType())).thenReturn(new PageImpl<>(Collections.singletonList(new PostResponce())));

        // When
        Page<PostResponce> result = postServiceImpl.findAll(0, 10);

        // Then
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(page.getTotalElements(), result.getTotalElements());

        // Add more assertions as needed
    }
    @Test
    void testFindAll_Exception() {
        // Mock behavior
        when(postRepository.findAllPost(any())).thenReturn(Page.empty());

        // Test method and verify exception
        assertThrows(CustomException.class, () -> postServiceImpl.findAll(0, 10));
        verify(postRepository, times(1)).findAllPost(any());
    }

    @Test
    public void testGetById() {
        // Given
        when(postRepository.findById(1)).thenReturn(Optional.of(post));

        // When
        Post result = postServiceImpl.getById(1);

        // Then
        assertNotNull(result);
        assertEquals("testNickname", result.getName());
        // Add more assertions as needed
    }
    @Test
    public void testGetById_NotFound() {
        // Arrange
        int postId = 1;
        when(postRepository.findById(eq(postId))).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(CustomException.class, () -> postServiceImpl.getById(postId));
    }

*/
/*    @Test
    public void testCreatePost() {
        // Mock Profile Response
        ProfileResponce profileResponse = new ProfileResponce();
        profileResponse.setId(123);
        profileResponse.setNickname("TestNickname");

        // Mock Profile Client
        when(profileClient.getById(anyInt())).thenReturn(profileResponse);

        // Mock Post Repository
        when(postRepository.save(any(Post.class))).thenReturn(post);

        // Call the method
        postServiceImpl.createPost(post);

        // Verify interactions
        verify(profileClient, times(1)).getById(anyInt());
        verify(postRepository, times(1)).save(any(Post.class));
        verify(postProducerMQ, times(1)).sendPostDataToRabbitMQ(any(PostSubscriberMessage.class));
    }*//*


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
        postServiceImpl.updatePost(postId, updateRequest);

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
        assertThrows(CustomException.class, () -> postServiceImpl.updatePost(postId, updateRequest));
    }

    @Test
    void deleteById_PostExists_DeletionSuccessful() {
        // Arrange
        int postId = 1;

        // Act
        postServiceImpl.deleteById(postId);

        // Assert
        verify(postRepository).deleteById(postId);
    }
}*/
