/*
package com.example.profileservice.service;

import com.example.plannerentity.dto.responce.ProfileResponce;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.keycloak.admin.client.resource.RealmResource;
import com.example.profileservice.model.Profile;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.UserRepresentation;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.*;

import com.example.plannerentity.global_exception.CustomException;

import javax.ws.rs.core.Response;

import com.example.profileservice.repository.ProfileRepository;
import org.keycloak.admin.client.Keycloak;
import com.example.plannerentity.dto.request.ProfileUpdateRequest;

import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.HttpStatus;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Disabled;

@ExtendWith(MockitoExtension.class)
class ProfileServiceSapientGeneratedTest {

    @Mock
    private ProfileRepository profileRepository;
    @Mock
    private ModelMapper modelMapper;
    @Mock
    private Keycloak keycloak;
    @Mock
    private RabbitTemplate rabbitTemplate;
    @InjectMocks
    private ProfileService profileService;

    @Test
    void updateUser_ProfileFound_Success() {
        String authId = "authId";
        ProfileUpdateRequest updateRequest = new ProfileUpdateRequest();

        JwtAuthenticationToken jwtToken = mock(JwtAuthenticationToken.class);
        given(jwtToken.getTokenAttributes()).willReturn(Collections.singletonMap("sub", authId)); // return a map with "sub" attribute

        Profile existingProfile = new Profile();
        existingProfile.setAuth_id(authId);

        RealmResource realmResourceMock = mock(RealmResource.class);
        UserResource userResourceMock = mock(UserResource.class);

        when(profileRepository.findProfileByAuth_id(authId)).thenReturn(Optional.of(existingProfile));
        when(keycloak.realm("test")).thenReturn(realmResourceMock); // return the mock RealmResource
        when(realmResourceMock.users()).thenReturn(mock(UsersResource.class)); // assuming users() returns UsersResource
        when(realmResourceMock.users().get(authId)).thenReturn(userResourceMock); // return the mock UserResource

        profileService.updateUser(updateRequest, jwtToken);

        verify(profileRepository).findProfileByAuth_id(authId);
        verify(profileRepository).save(existingProfile);
        verify(userResourceMock).update(any(UserRepresentation.class)); // verify on the UserResource mock
        verify(rabbitTemplate).convertAndSend(anyString(), anyString(), any(Profile.class));

    }
    @Test
    void testCreateProfile() {
        Profile profile = new Profile();
        profile.setBio("Some bio");

        JwtAuthenticationToken token = mock(JwtAuthenticationToken.class);
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("preferred_username", "testUser");
        attributes.put("sub", "userId123");
        when(token.getTokenAttributes()).thenReturn(attributes);

        // When
        profileService.createProfile(profile, token);

        // Then
        verify(profileRepository).save(profile);
        assertEquals("testUser", profile.getNickname());
        assertEquals("userId123", profile.getAuth_id());
    }

    @Test
    public void testGetById() {
        // Given
        int profileId = 1;
        Profile profile = new Profile();
        profile.setId(profileId);
        profile.setNickname("testUser");
        when(profileRepository.findById(profileId)).thenReturn(Optional.of(profile));

        ProfileResponce expectedResponse = new ProfileResponce();
        expectedResponse.setId(profile.getId());
        expectedResponse.setNickname(profile.getNickname());
        when(modelMapper.map(profile, ProfileResponce.class)).thenReturn(expectedResponse);

        // When
        ProfileResponce actualResponse = profileService.getById(profileId);

        // Then
        assertEquals(expectedResponse, actualResponse);
    }



    @Test
    void deleteById_ProfileFound_Success() {
        // Arrange
        int id = 1;
        String authId = "authId";
        JwtAuthenticationToken jwtToken = mock(JwtAuthenticationToken.class);
        given(jwtToken.getTokenAttributes()).willReturn(Collections.singletonMap("sub", authId)); // return a map with "sub" attribute

        Profile profile = new Profile();
        profile.setId(id);

        RealmResource realmResourceMock = mock(RealmResource.class);
        UsersResource usersResourceMock = mock(UsersResource.class);

        when(keycloak.realm("test")).thenReturn(realmResourceMock); // return the mock RealmResource
        when(realmResourceMock.users()).thenReturn(usersResourceMock); // assuming users() returns UsersResource

        // Act
        profileService.deleteById(id, jwtToken);

        // Assert
        verify(profileRepository).deleteById(id);
        verify(usersResourceMock).delete(authId);
    }

}
*/
