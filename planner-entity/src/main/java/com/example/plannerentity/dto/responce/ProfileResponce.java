package com.example.plannerentity.dto.responce;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfileResponce {

    int id;
    String nickname;
    String bio;
    String email;
    //List<Integer> subscribers;
}
