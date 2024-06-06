package com.example.postservice.service.impl;

import com.example.plannerentity.dto.request.PostCreatedRequest;
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
import com.example.postservice.service.S3FileService;
import com.example.postservice.service.impl.PostServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.multipart.MultipartFile;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)

class PostServiceImplTest {
    @Mock
    private PostRepository postRepository;
    @Mock
    private ModelMapper modelMapper;
    @Mock
    private PostProducerMQ postProducerMQ;
    @Mock
    private ProfileClient profileClient;
    @Mock
    S3FileService s3FileService;
    @Mock
    private Authentication authentication;
    @Mock
    private JwtAuthenticationToken jwtAuthenticationToken;
    @InjectMocks
    private PostServiceImpl postServiceImpl;
    private Post post;
    private ProfileResponce profile;
    private PostCreatedRequest createdRequest;
    private PostUpdateRequest updateRequest;

    @BeforeEach
    void setUp() {
        jwtAuthenticationToken = mock(JwtAuthenticationToken.class);
        when(jwtAuthenticationToken.getTokenAttributes()).thenReturn(Map.of(
                "preferred_username", "testUser"

        ));

        authentication = jwtAuthenticationToken;
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);


        post = new Post();
        post.setId(1);
        post.setName("Test Post");
        post.setContent("Test Content");
        post.setPhotoUrls(new ArrayList<>());

        profile = new ProfileResponce();
        profile.setId(1);
        profile.setNickname("testUser");
        profile.setAuth_id("testAuthId");

        createdRequest = new PostCreatedRequest();
       // createdRequest.setAccountId(1);
        createdRequest.setName("Test Post");
        createdRequest.setContent("Test Content");

        updateRequest = new PostUpdateRequest();
        updateRequest.setName("Updated Post");
        updateRequest.setContent("Updated Content");
        updateRequest.setCategory(Category.IT);
    }


    @Test
    void testFindAllWithNoResults() {
        when(postRepository.findAllPost(any(Pageable.class))).thenReturn(Page.empty());

        assertThrows(CustomException.class, () -> postServiceImpl.findAll(0, 10));

        verify(postRepository, times(1)).findAllPost(any(Pageable.class));
        verify(modelMapper, never()).map(any(Page.class), any(Type.class));
    }

    @Test
    void testGetByNicknameWithNoResults() {
        when(postRepository.findAllByAuthor(anyString(), any(Pageable.class))).thenReturn(Page.empty());

        assertThrows(CustomException.class, () -> postServiceImpl.getByNickname("testUser", 0, 10));

        verify(postRepository, times(1)).findAllByAuthor("testUser", PageRequest.of(0, 10));
        verify(modelMapper, never()).map(any(Page.class), any(Type.class));
    }

    @Test
    void testGetByNameWithNoResults() {
        when(postRepository.findAllByName(anyString(), any(Pageable.class))).thenReturn(Page.empty());

        assertThrows(CustomException.class, () -> postServiceImpl.getByName("Test Post", 0, 10));

        verify(postRepository, times(1)).findAllByName("Test Post", PageRequest.of(0, 10));
        verify(modelMapper, never()).map(any(Page.class), any(Type.class));
    }

    @Test
    void testGetByIdWithNoResults() {
        when(postRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(CustomException.class, () -> postServiceImpl.getById(1));

        verify(postRepository, times(1)).findById(1);
    }

    @Test
    void testFindByCategoryWithNoResults() {
        when(postRepository.findByCategory(any(Category.class), any(Pageable.class))).thenReturn(Page.empty());

        assertThrows(CustomException.class, () -> postServiceImpl.findByCategory(Category.IT, 0, 10));

        verify(postRepository, times(1)).findByCategory(Category.IT, PageRequest.of(0, 10));
        verify(modelMapper, never()).map(any(Page.class), any(Type.class));
    }

    @Test
    void testDeletePhotoByIdWithInvalidPhoto() {
        when(postRepository.findById(1)).thenReturn(Optional.of(post));

        assertThrows(CustomException.class, () -> postServiceImpl.deletePhotoById(1, "invalidUrl"));

        verify(postRepository, times(1)).findById(1);
        verify(s3FileService, never()).deleteFileFromS3Bucket(anyString());
        verify(postRepository, never()).save(any(Post.class));
    }

    @Test
    void testDeletePhotoByIdWithPostNotFound() {
        when(postRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(CustomException.class, () -> postServiceImpl.deletePhotoById(1, "testUrl"));

        verify(postRepository, times(1)).findById(1);
        verify(s3FileService, never()).deleteFileFromS3Bucket(anyString());
        verify(postRepository, never()).save(any(Post.class));
    }


    @Test
    void testDeleteById() {
        postServiceImpl.deleteById(1);
        verify(postRepository, times(1)).deleteById(1);
    }

    @Test
    void testFindByCategory() {
        Page<Post> postPage = new PageImpl<>(List.of(post));
        when(postRepository.findByCategory(any(Category.class), any(Pageable.class))).thenReturn(postPage);
        //when(modelMapper.map(any(Page.class), any(Type.class))).thenReturn(new PageImpl<>(List.of(new PostResponce())));

        Page<Post> result = postServiceImpl.findByCategory(Category.IT, 0, 10);

        verify(postRepository, times(1)).findByCategory(Category.IT, PageRequest.of(0, 10));
       // verify(modelMapper, times(1)).map(any(Page.class), any(Type.class));
        assertFalse(result.isEmpty());
    }

    @Test
    void testGetByNickname() {
        Page<Post> postPage = new PageImpl<>(List.of(post));
        when(postRepository.findAllByAuthor(anyString(), any(Pageable.class))).thenReturn(postPage);
       // when(modelMapper.map(any(Page.class), any(Type.class))).thenReturn(new PageImpl<>(List.of(new PostResponce())));

        Page<Post> result = postServiceImpl.getByNickname("testUser", 0, 10);

        verify(postRepository, times(1)).findAllByAuthor("testUser", PageRequest.of(0, 10));
        //verify(modelMapper, times(1)).map(any(Page.class), any(Type.class));
        assertFalse(result.isEmpty());
    }

    @Test
    void testGetByName() {
        Page<Post> postPage = new PageImpl<>(List.of(post));
        when(postRepository.findAllByName(anyString(), any(Pageable.class))).thenReturn(postPage);

        Page<Post> result = postServiceImpl.getByName("Test Post", 0, 10);

        verify(postRepository, times(1)).findAllByName("Test Post", PageRequest.of(0, 10));
        assertFalse(result.isEmpty());
    }

 @Test
 void testFindAll() {
     Page<Post> postPage = new PageImpl<>(List.of(post));
     when(postRepository.findAllPost(any(Pageable.class))).thenReturn(postPage);

     Page<Post> result = postServiceImpl.findAll(0, 10);

     verify(postRepository).findAllPost(any(Pageable.class));
     assertFalse(result.isEmpty());
 }

    @Test
    void testFindAllWhenNoPostsFound() {
        // Mocks
        when(postRepository.findAllPost(any(PageRequest.class))).thenReturn(new PageImpl<>(new ArrayList<>()));

        // Method call and assertion
        CustomException exception = assertThrows(CustomException.class, () -> postServiceImpl.findAll(0, 10));
        assertEquals("Posts not found", exception.getMessage());
    }


    @Test
    void testAddPhotoById() {
        List<MultipartFile> files = new ArrayList<>();
        files.add(new MockMultipartFile("file1.jpg", new byte[]{1, 2, 3}));
        files.add(new MockMultipartFile("file2.jpg", new byte[]{4, 5, 6}));

        when(postRepository.findById(1)).thenReturn(Optional.of(post));
        when(s3FileService.uploadFile(any(MultipartFile.class))).thenReturn("uploaded_url");

        postServiceImpl.addPhotoById(1, files);

        verify(postRepository, times(1)).findById(1);
        verify(s3FileService, times(2)).uploadFile(any(MultipartFile.class));
        verify(postRepository, times(1)).save(post);
    }

    @Test
    void testGetById() {
        when(postRepository.findById(any(Integer.class))).thenReturn(Optional.of(post));

        Post result = postServiceImpl.getById(1);

        assertNotNull(result);
        assertEquals(post, result);
        verify(postRepository, times(1)).findById(1);
    }



    @Test
    void testCreatePost() {
        when(profileClient.getByNickname("testUser")).thenReturn(profile);
        when(modelMapper.map(any(PostCreatedRequest.class), eq(Post.class))).thenReturn(post);
        when(postRepository.save(any(Post.class))).thenReturn(post);

        postServiceImpl.createPost(createdRequest);

        verify(modelMapper, times(1)).map(createdRequest, Post.class);
        verify(postRepository, times(1)).save(post);
        verify(postProducerMQ, times(1)).sendPostDataToRabbitMQ(any(PostSubscriberMessage.class));
    }

    @Test
    void testUpdatePost() {
        when(postRepository.findById(any(Integer.class))).thenReturn(Optional.of(post));

        postServiceImpl.updatePost(1, updateRequest);

        verify(postRepository, times(1)).findById(1);
        verify(postRepository, times(1)).save(any(Post.class));
    }
    @Test
    void testDeletePhotoById() {
        List<String> photoUrls = new ArrayList<>();
        photoUrls.add("photo1.jpg");
        photoUrls.add("photo2.jpg");
        post.setPhotoUrls(photoUrls);

        when(postRepository.findById(1)).thenReturn(Optional.of(post));

        postServiceImpl.deletePhotoById(1, "photo2.jpg");

        verify(postRepository, times(1)).findById(1);
        verify(s3FileService, times(1)).deleteFileFromS3Bucket("photo2.jpg");
        verify(postRepository, times(1)).save(post);
    }



}
