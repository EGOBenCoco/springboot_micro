package com.example.authservice.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Customer {

    int id;
    String email;
    String nickname;
    String password;


    Set<Role> roles=new HashSet<>();


    //String roles;
}
