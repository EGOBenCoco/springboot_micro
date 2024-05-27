package com.example.profileservice.service.impl;

import com.example.plannerentity.dto.request.ProfileCreatedRequest;
import com.example.plannerentity.dto.request.ProfileUpdateRequest;
import com.example.plannerentity.dto.responce.ProfileResponce;
import com.example.plannerentity.global_exception.CustomException;
import com.example.profileservice.model.Profile;
import com.example.profileservice.producer_mq.ProfileProducerMQ;
import com.example.profileservice.repository.ProfileRepository;
import com.example.profileservice.service.impl.ProfileServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.Map;
import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ProfileServiceTest {

    @Mock
    private ProfileRepository profileRepository;
    @Mock
    private ModelMapper modelMapper;
    @Mock
    private Keycloak keycloak;
    @Mock
    ProfileProducerMQ profileProducerMQ;
    @Mock
    private Authentication authentication;
    @Mock
    private RealmResource realmResource;
    @Mock
    private UserResource userResource;
    @Mock
    private JwtAuthenticationToken jwtAuthenticationToken;
    @InjectMocks
    private ProfileServiceImpl profileServiceImpl;

    private ProfileCreatedRequest profileCreatedRequest;

    private ProfileUpdateRequest profileUpdateRequest;
    private Profile profile;


    @Value("${variables.keycloak.realm}")
    String realm;

    @BeforeEach
    public void setup() {
        jwtAuthenticationToken = mock(JwtAuthenticationToken.class);
        when(jwtAuthenticationToken.getTokenAttributes()).thenReturn(Map.of(
                "email", "user@example.com",
                "sub", "auth_id_example"
        ));
        authentication = jwtAuthenticationToken;
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(keycloak.realm(realm)).thenReturn(realmResource);
        when(realmResource.users()).thenReturn(mock(UsersResource.class));
        when(realmResource.users().get(anyString())).thenReturn(userResource);

        profileCreatedRequest = new ProfileCreatedRequest();
        profileCreatedRequest.setBio("Test Bio");

        profile = new Profile();
        profile.setNickname("Test user");
    }

    @Test
    public void testCreateProfile() {
        when(modelMapper.map(profileCreatedRequest, Profile.class)).thenReturn(profile);
        when(profileRepository.existsByAuth_id(anyString())).thenReturn(false);

        profileServiceImpl.createProfile(profileCreatedRequest);

        verify(profileRepository, times(1)).save(profile);
    }

    @Test
    public void testCreateProfileProfileExists() {
        when(profileRepository.existsByAuth_id(anyString())).thenReturn(true);
        assertThrows(CustomException.class, () -> profileServiceImpl.createProfile(profileCreatedRequest));
    }

    @Test
    public void testGetById() {
        Profile profile = new Profile();
        when(profileRepository.findById(1)).thenReturn(Optional.of(profile));

        ProfileResponce profileResponce = new ProfileResponce();
        when(modelMapper.map(profile, ProfileResponce.class)).thenReturn(profileResponce);

        ProfileResponce result = profileServiceImpl.getById(1);

        assertEquals(profileResponce, result);
        assertNotNull(result);
    }

    @Test
    void updateUser_whenProfileExists_updatesProfile() {
        // Arrange
        String authId = "auth_id_example";
        ProfileUpdateRequest updateRequest = new ProfileUpdateRequest();
        updateRequest.setNickname("NewNickname");
        updateRequest.setBio("NewBio");

        Profile existingProfile = new Profile();
        existingProfile.setAuth_id(authId);
        existingProfile.setNickname("OldNickname");
        existingProfile.setBio("OldBio");

        when(profileRepository.findProfileByAuth_id(authId)).thenReturn(Optional.of(existingProfile));
        // Mock Keycloak and other behavior if necessary

        // Act
        profileServiceImpl.updateUser(updateRequest);

        // Assert
        verify(profileRepository).save(existingProfile);
        verify(profileProducerMQ).sendMessage(existingProfile.getId(), existingProfile.getNickname());
        assertEquals("NewNickname", existingProfile.getNickname());
        assertEquals("NewBio", existingProfile.getBio());
    }

    @Test
    public void testUpdateUser_ProfileNotExists() {
        // Arrange
        ProfileUpdateRequest updateRequest = new ProfileUpdateRequest(/* provide necessary data */);
        when(profileRepository.findProfileByAuth_id(anyString())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(CustomException.class, () -> profileServiceImpl.updateUser(updateRequest));
    }



    @Test
    public void testDeleteById_Success() throws Exception {
        int userIdToDelete = 123;

        doNothing().when(profileRepository).deleteById(userIdToDelete);

        doNothing().when(userResource).remove();

        profileServiceImpl.deleteById(userIdToDelete);

        verify(profileRepository).deleteById(userIdToDelete);
    }
}