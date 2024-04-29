/*
package com.example.profileservice.service;

import com.example.plannerentity.dto.request.ProfileUpdateRequest;
import com.example.plannerentity.dto.responce.ProfileResponce;
import com.example.plannerentity.global_exception.CustomException;
import com.example.profileservice.model.Profile;
import com.example.profileservice.producer_mq.ProfileProducerMQ;
import com.example.profileservice.repository.ProfileRepository;
import com.example.profileservice.service.impl.ProfileServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.UserRepresentation;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProfileServiceSapientGeneratedTest {

    @Mock
    private ProfileRepository profileRepository;
    @Mock
    private ModelMapper modelMapper;
    @Mock
    private Keycloak keycloak;
    @Mock
    ProfileProducerMQ profileProducerMQ;
    @Mock
    private JwtAuthenticationToken token;
    @InjectMocks
    private ProfileServiceImpl profileServiceImpl;
    @Test
    public void testCreateProfile() {
        // Arrange
        Profile profile = new Profile();
        profile.setNickname("testNickname");
        profile.setAuth_id("testAuthId");

        // Мокирование возвращаемого значения метода getTokenAttributes() как Map
        Map<String, Object> tokenAttributes = new HashMap<>();
        tokenAttributes.put("preferred_username", "testNickname");
        tokenAttributes.put("sub", "testAuthId");
        when(token.getTokenAttributes()).thenReturn(tokenAttributes);

        // Act
        profileServiceImpl.createProfile(profile, token);

        // Assert
        verify(profileRepository, times(1)).save(profile);
    }

 */
/*   @Test
    public void testGetById() {
        // Создание заглушки Profile
        Profile profile = new Profile();
        when(profileRepository.findById(1)).thenReturn(java.util.Optional.of(profile));

        // Создание заглушки ProfileResponce
        ProfileResponce profileResponce = new ProfileResponce();
        when(modelMapper.map(profile, ProfileResponce.class)).thenReturn(profileResponce);

        ProfileResponce result = profileServiceImpl.getById(1);

        // Проверка, что результат соответствует ожидаемому значению
        assertEquals(profileResponce, result);
    }*//*


    @Test
    public void testGetById_ProfileNotFound() {
        // Arrange
        int id = 1;
        // Мокирование поведения ProfileRepository
        when(profileRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(CustomException.class, () -> profileServiceImpl.getById(id));
    }

    @Test
    void updateUser_ProfileFound_Success() {
        String authId = "authId";
        ProfileUpdateRequest updateRequest = new ProfileUpdateRequest();

        JwtAuthenticationToken jwtToken = mock(JwtAuthenticationToken.class);
        given(jwtToken.getTokenAttributes()).willReturn(Collections.singletonMap("sub", authId));

        Profile existingProfile = new Profile();
        existingProfile.setAuth_id(authId);

        RealmResource realmResourceMock = mock(RealmResource.class);
        UserResource userResourceMock = mock(UserResource.class);

        when(profileRepository.findProfileByAuth_id(authId)).thenReturn(Optional.of(existingProfile));
        when(keycloak.realm("test")).thenReturn(realmResourceMock);
        when(realmResourceMock.users()).thenReturn(mock(UsersResource.class));
        when(realmResourceMock.users().get(authId)).thenReturn(userResourceMock);

        profileServiceImpl.updateUser(updateRequest, jwtToken);

        verify(profileRepository).findProfileByAuth_id(authId);
        verify(profileRepository).save(existingProfile);
        verify(userResourceMock).update(any(UserRepresentation.class));
        verify(profileProducerMQ).sendMessage( any(Profile.class));

    }
    @Test
    void updateUser_ProfileNotFound_ExceptionThrown() {
        String authId = "authId";
        ProfileUpdateRequest updateRequest = new ProfileUpdateRequest();

        JwtAuthenticationToken jwtToken = mock(JwtAuthenticationToken.class);
        given(jwtToken.getTokenAttributes()).willReturn(Collections.singletonMap("sub", authId));

        when(profileRepository.findProfileByAuth_id(authId)).thenReturn(Optional.empty());

        // Проверяем, что при вызове метода updateUser с несуществующим профилем будет выброшено исключение RuntimeException
        assertThrows(RuntimeException.class, () -> profileServiceImpl.updateUser(updateRequest, jwtToken));

        // Проверяем, что методы не были вызваны
        verify(profileRepository).findProfileByAuth_id(authId);
        verify(profileRepository, never()).save(any());

    }





    @Test
    public void deleteById_ProfileFound_Success() {
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
        profileServiceImpl.deleteById(id, jwtToken);

        // Assert
        verify(profileRepository).deleteById(id);
        verify(usersResourceMock).delete(authId);
    }

}
   */
/* @Test
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
*//*


*/
