package com.example.profileservice.controller;

import com.example.plannerentity.dto.request.ProfileUpdateRequest;
import com.example.plannerentity.dto.responce.ProfileResponce;
import com.example.profileservice.model.Profile;
import com.example.profileservice.service.ProfileService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/profiles")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class ProfileController {
    ProfileService profileService;

    @PostMapping
    public ResponseEntity<String> create(@RequestBody Profile profile, Principal principal){
        profileService.createProfile(profile,principal);
        return ResponseEntity.ok("Your profile created");
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProfileResponce> getById(@PathVariable int id){
        return ResponseEntity.ok(profileService.getById(id));
    }

    @PutMapping()
    public ResponseEntity<String> changeUsername(@RequestBody ProfileUpdateRequest username, Principal principal){
        profileService.updateUser(username,principal);
        return ResponseEntity.ok("Your nickname updated");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteById(@PathVariable int id,Principal principal){
        profileService.deleteById(id,principal);
        return ResponseEntity.ok("Your account deleted");
    }


}
