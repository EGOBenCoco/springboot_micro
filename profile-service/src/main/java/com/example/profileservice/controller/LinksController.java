package com.example.profileservice.controller;

import com.example.plannerentity.global_exception.SuccessMessage;
import com.example.profileservice.model.Link;
import com.example.profileservice.service.LinkService;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/links")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class LinksController {

    LinkService linkService;

    @PostMapping
    public ResponseEntity<Object> createLink(@RequestBody Link link){
        linkService.createLink(link);
        return ResponseEntity.ok(SuccessMessage.builder()
                .status(HttpStatus.OK.value())
                .message("Link created")
                .datetime(LocalDateTime.now())
                .build());
    }

    @PutMapping
    public ResponseEntity<Object> updateLink(@RequestBody Link link){
        linkService.updateLink(link);
        return ResponseEntity.ok(SuccessMessage.builder()
                .status(HttpStatus.OK.value())
                .message("Link updated")
                .datetime(LocalDateTime.now())
                .build());
    }

    @DeleteMapping("/{linkId}")
    public ResponseEntity<Object> updateLink(@PathVariable int linkId){
        linkService.deleteLink(linkId);
        return ResponseEntity.ok(SuccessMessage.builder()
                .status(HttpStatus.OK.value())
                .message("Link deleted")
                .datetime(LocalDateTime.now())
                .build());
    }




}
