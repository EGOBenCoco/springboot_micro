package com.example.profileservice.service;

import com.example.plannerentity.dto.request.ProfileUpdateRequest;
import com.example.plannerentity.dto.responce.ProfileResponce;
import com.example.plannerentity.global_exception.CustomException;
import com.example.profileservice.model.Profile;
import com.example.profileservice.repository.ProfileRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.modelmapper.ModelMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProfileService {

    ProfileRepository profileRepository;
    ModelMapper modelMapper;
    Keycloak keycloak;
    RabbitTemplate rabbitTemplate;

    public void createProfile(Profile profile, Principal principal) {
        JwtAuthenticationToken token = (JwtAuthenticationToken) principal;
        String nickname = (String) token.getTokenAttributes().get("preferred_username");
        String auth_id = (String) token.getTokenAttributes().get("sub");
        profile.setNickname(nickname);
        profile.setAuth_id(auth_id);
        profileRepository.save(profile);
    }

    public ProfileResponce getById(int id) {
        Profile profile = profileRepository.findById(id).orElseThrow(()-> new CustomException("Profile not found",HttpStatus.NOT_FOUND));
        return modelMapper.map(profile, ProfileResponce.class);
    }

    public void updateUser(ProfileUpdateRequest user, Principal principal) {

        JwtAuthenticationToken token = (JwtAuthenticationToken) principal;
        String auth_id = (String) token.getTokenAttributes().get("sub");
        profileRepository.findProfileByAuth_id(auth_id).ifPresentOrElse(consumer ->
        {
            consumer.setNickname(user.getNickname());
            consumer.setBio(user.getBio());
            profileRepository.save(consumer);
            UserRepresentation userRep = mapUserRep(user);
            keycloak.realm("test").users().get(auth_id).update(userRep);
            rabbitTemplate.convertAndSend("topic_exchange", "for.post", consumer);
        }, () -> {
            throw new RuntimeException("Consumer not found");
        });
    }

    public void deleteById(int id,Principal principal){
        JwtAuthenticationToken token = (JwtAuthenticationToken) principal;
        String auth_id = (String) token.getTokenAttributes().get("sub");
        profileRepository.deleteById(id);
        keycloak.realm("test").users().delete(auth_id);
    }



    private UserRepresentation mapUserRep(ProfileUpdateRequest user) {
        UserRepresentation userRep = new UserRepresentation();
        //userRep.setId(user.getAuth_id());
        userRep.setUsername(user.getNickname());
        userRep.setEnabled(true);
        userRep.setEmailVerified(true);
        List<CredentialRepresentation> creds = new ArrayList<>();
        CredentialRepresentation cred = new CredentialRepresentation();
        cred.setTemporary(false);
        creds.add(cred);
        userRep.setCredentials(creds);
        return userRep;
    }


}
