package com.example.authservice.models;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class AuthRequest {
    private String email;
    private String password;
    private String nickname;
}