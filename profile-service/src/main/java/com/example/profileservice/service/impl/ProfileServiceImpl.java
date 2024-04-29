package com.example.profileservice.service.impl;

import com.example.plannerentity.dto.request.ProfileUpdateRequest;
import com.example.plannerentity.dto.responce.ProfileResponce;
import com.example.plannerentity.global_exception.CustomException;
import com.example.profileservice.model.Profile;
import com.example.profileservice.producer_mq.ProfileProducerMQ;
import com.example.profileservice.repository.ProfileRepository;
import com.example.profileservice.service.ProfileService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProfileServiceImpl implements ProfileService {

    ProfileRepository profileRepository;
    ModelMapper modelMapper;
    Keycloak keycloak;
    ProfileProducerMQ profileProducerMQ;

    public void createProfile(Profile profile, Principal principal) {
        String nickname = getNickname(principal);
        String auth_id = getAuth_Id(principal);
        if(profileRepository.existsByAuth_id(auth_id)){
            throw new CustomException("Profile has already been created",HttpStatus.BAD_REQUEST);
        }
        profile.setNickname(nickname);
        profile.setAuth_id(auth_id);
        profileRepository.save(profile);
    }

    public ProfileResponce getById(int id) {
        Profile profile = profileRepository.findById(id).orElseThrow(()-> new CustomException("Profile not found",HttpStatus.NOT_FOUND));
        return modelMapper.map(profile, ProfileResponce.class);
    }

    public void updateUser(ProfileUpdateRequest user, Principal principal) {
        String auth_id = getAuth_Id(principal);
        profileRepository.findProfileByAuth_id(auth_id).ifPresentOrElse(consumer ->
        {
            consumer.setNickname(user.nickname());
            consumer.setBio(user.bio());
            UserRepresentation userRep = mapUserRep(user);
            keycloak.realm("test").users().get(auth_id).update(userRep);
            profileRepository.save(consumer);
            profileProducerMQ.sendMessage(consumer);
        }, () -> {
            throw new RuntimeException("Consumer not found");
        });
    }

    public void deleteById(int id,Principal principal){
        String auth_id = getAuth_Id(principal);
        profileRepository.deleteById(id);
        keycloak.realm("test").users().delete(auth_id);
    }
    private UserRepresentation mapUserRep(ProfileUpdateRequest user) {
        UserRepresentation userRep = new UserRepresentation();
        //userRep.setId(user.getAuth_id());
        userRep.setUsername(user.nickname());
        userRep.setEnabled(true);
        userRep.setEmailVerified(true);
        List<CredentialRepresentation> creds = new ArrayList<>();
        CredentialRepresentation cred = new CredentialRepresentation();
        cred.setTemporary(false);
        creds.add(cred);
        userRep.setCredentials(creds);
        return userRep;
    }
    private String getAuth_Id(Principal principal){
        JwtAuthenticationToken token = (JwtAuthenticationToken) principal;
        return (String) token.getTokenAttributes().get("sub");
    }

    private String getNickname(Principal principal){
        JwtAuthenticationToken token = (JwtAuthenticationToken) principal;
        return (String) token.getTokenAttributes().get("preferred_username");
    }
}
