package com.example.authservice.models;

import lombok.Data;

@Data
public class LoginRequest {

    String email;
    String password;
}
