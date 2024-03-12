package com.example.authservice.service;

import com.example.authservice.client.CustomerClient;
import com.example.authservice.models.AuthRequest;
import com.example.authservice.models.AuthResponse;
import com.example.authservice.models.Customer;
import com.example.authservice.models.LoginRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class AuthService {

    JwtUtil jwt;
    CustomerClient customerClient;
/*    public void register(AuthRequest authRequest) {
        //do validation if user already exists
        authRequest.setPassword(BCrypt.hashpw(authRequest.getPassword(), BCrypt.gensalt()));

        Customer userVO = customerClient.create(authRequest);
        Assert.notNull(userVO, "Failed to register user. Please try again later");

        *//*String accessToken = jwt.generate(userVO, "ACCESS");
        String refreshToken = jwt.generate(userVO, "REFRESH");

        return new AuthResponse(accessToken, refreshToken);*//*

    }*/


    public AuthResponse login(LoginRequest loginRequest){

        Customer customer = customerClient.getByEmail(loginRequest.getEmail());

        String accessToken = jwt.generate(customer, "ACCESS");
        String refreshToken = jwt.generate(customer, "REFRESH");
        return new AuthResponse(accessToken, refreshToken);
    }
}