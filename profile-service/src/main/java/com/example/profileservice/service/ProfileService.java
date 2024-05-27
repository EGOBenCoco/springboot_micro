package com.example.profileservice.service;

import com.example.plannerentity.dto.request.ProfileCreatedRequest;
import com.example.plannerentity.dto.request.ProfileUpdateRequest;
import com.example.plannerentity.dto.responce.ProfileResponce;
import com.example.profileservice.model.Profile;

import java.security.Principal;

public interface ProfileService {
    void createProfile(ProfileCreatedRequest profile);

    ProfileResponce getById(int id);

    ProfileResponce getByNickname(String nickname);

    void updateUser(ProfileUpdateRequest user);

    void deleteById(int id);
    void updatePassword(String userId);
}
