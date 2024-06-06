package com.example.profileservice.service.impl;

import com.example.plannerentity.dto.request.ProfileCreatedRequest;
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
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class ProfileServiceImpl implements ProfileService {

    ProfileRepository profileRepository;
    ModelMapper modelMapper;
    Keycloak keycloak;
    ProfileProducerMQ profileProducerMQ;

    @Value("${variables.keycloak.realm}")
    String realm;

    public ProfileServiceImpl(ProfileRepository profileRepository,
                              ModelMapper modelMapper,
                              Keycloak keycloak,
                              ProfileProducerMQ profileProducerMQ) {
        this.profileRepository = profileRepository;
        this.modelMapper = modelMapper;
        this.keycloak = keycloak;
        this.profileProducerMQ = profileProducerMQ;

    }


    public void createProfile(ProfileCreatedRequest createdRequest) {
        String nickname = getTokenAttribute("preferred_username");
        String auth_id = getTokenAttribute("sub");
        Profile profile = modelMapper.map(createdRequest, Profile.class);
        if (profileRepository.existsByAuth_id(auth_id)) {
            throw new CustomException("Your profile has already been created", HttpStatus.BAD_REQUEST);
        }
        profile.setNickname(nickname);
        profile.setAuth_id(auth_id);
        profileRepository.save(profile);
    }


    public ProfileResponce getById(int id) {
        Profile profile = profileRepository.findById(id).orElseThrow(() -> new CustomException("Profile not found", HttpStatus.NOT_FOUND));
        return modelMapper.map(profile, ProfileResponce.class);
    }


    @Override
    public ProfileResponce getByNickname(String nickname) {
        Profile profile = profileRepository.findByNickname(nickname).orElseThrow(() -> new CustomException("Profile not found", HttpStatus.NOT_FOUND));
        return modelMapper.map(profile, ProfileResponce.class);
    }

    public void updateProfile(ProfileUpdateRequest updateRequest) {
        String auth_id = getTokenAttribute("sub");
        profileRepository.findProfileByAuth_id(auth_id).ifPresentOrElse(profile ->
        {
            profile.setNickname(updateRequest.getNickname());
            profile.setBio(updateRequest.getBio());
            UserRepresentation userRep = mapUserRep(updateRequest);
            keycloak.realm(realm).users().get(auth_id).update(userRep);
            profileRepository.save(profile);
            profileProducerMQ.sendMessage(profile.getId(),profile.getNickname());
        }, () -> {
            throw new CustomException("Profile not found", HttpStatus.NOT_FOUND);
        });
    }

    @Transactional
    public void deleteById(int id) {
        String auth_id = getTokenAttribute("sub");
        profileRepository.deleteById(id);
        keycloak.realm(realm).users().delete(auth_id);
    }

    public void updatePassword(String userId) {
        UserResource userResource = getUserResource(userId);
        List<String> actions = new ArrayList<>();
        actions.add("UPDATE_PASSWORD");
        userResource.executeActionsEmail(actions);
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

    private String getTokenAttribute(String arg) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        JwtAuthenticationToken token = (JwtAuthenticationToken) authentication;
        return (String) token.getTokenAttributes().get(arg);
    }



    private UserResource getUserResource(String userId) {
        UsersResource usersResource = getUsersResource();
        return usersResource.get(userId);
    }

    private UsersResource getUsersResource() {
        RealmResource realm1 = keycloak.realm(realm);
        return realm1.users();
    }
}
