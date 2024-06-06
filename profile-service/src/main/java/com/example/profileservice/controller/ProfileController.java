package com.example.profileservice.controller;

import com.example.plannerentity.dto.request.ProfileCreatedRequest;
import com.example.plannerentity.dto.request.ProfileUpdateRequest;
import com.example.plannerentity.dto.responce.ProfileResponce;
import com.example.plannerentity.global_exception.SuccessMessage;
import com.example.profileservice.service.ProfileService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/profiles")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class ProfileController {
    ProfileService profileService;

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody ProfileCreatedRequest profile){
        profileService.createProfile(profile);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(SuccessMessage.builder()
                        .status(HttpStatus.CREATED.value())
                        .message("Profile added")
                        .datetime(LocalDateTime.now())
                        .build());
    }

    @PutMapping("/update-password")
    public ResponseEntity<Object> updatePassword(Principal principal) {
        profileService.updatePassword(principal.getName());
        return ResponseEntity.status(HttpStatus.OK)
                .body(SuccessMessage.builder()
                        .message("Check your email")
                        .datetime(LocalDateTime.now())
                        .build());
    }


    @GetMapping("/{id}")
    public ResponseEntity<ProfileResponce> getById(@PathVariable int id){
        return ResponseEntity.ok(profileService.getById(id));
    }

    @GetMapping("/{nickname}/nickname")
    public ResponseEntity<ProfileResponce> getByNickname(@PathVariable String nickname){
        return ResponseEntity.ok(profileService.getByNickname(nickname));
    }

    @PutMapping
    public ResponseEntity<Object> changeUsername(@RequestBody ProfileUpdateRequest updateRequest){
        profileService.updateProfile(updateRequest);
        return ResponseEntity.ok(SuccessMessage.builder()
                .status(HttpStatus.OK.value())
                .message("Profile updated")
                .datetime(LocalDateTime.now())
                .build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteById(@PathVariable int id){
        profileService.deleteById(id);
        return ResponseEntity.ok(SuccessMessage.builder()
                .status(HttpStatus.OK.value())
                .message("Profile deleted")
                .datetime(LocalDateTime.now())
                .build());
    }


}
