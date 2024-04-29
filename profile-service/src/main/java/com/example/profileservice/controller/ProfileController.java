package com.example.profileservice.controller;

import com.example.plannerentity.dto.request.ProfileUpdateRequest;
import com.example.plannerentity.dto.responce.ProfileResponce;
import com.example.plannerentity.global_exception.SuccessMessage;
import com.example.profileservice.model.Profile;
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
    public ResponseEntity<Object> create(@RequestBody Profile profile, Principal principal){
        profileService.createProfile(profile,principal);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(SuccessMessage.builder()
                        .status(HttpStatus.CREATED.value())
                        .message("Profile added")
                        .datetime(LocalDateTime.now())
                        .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProfileResponce> getById(@PathVariable int id){
        return ResponseEntity.ok(profileService.getById(id));
    }

    @PutMapping
    public ResponseEntity<Object> changeUsername(@RequestBody ProfileUpdateRequest username, Principal principal){
        profileService.updateUser(username,principal);
        return ResponseEntity.ok(SuccessMessage.builder()
                .status(HttpStatus.OK.value())
                .message("Profile updated")
                .datetime(LocalDateTime.now())
                .build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteById(@PathVariable int id,Principal principal){
        profileService.deleteById(id,principal);
        return ResponseEntity.ok(SuccessMessage.builder()
                .status(HttpStatus.OK.value())
                .message("Profile deleted")
                .datetime(LocalDateTime.now())
                .build());
    }


}
