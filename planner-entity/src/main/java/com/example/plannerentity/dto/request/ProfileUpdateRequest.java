package com.example.plannerentity.dto.request;

import com.example.plannerentity.dto.responce.Link;
import lombok.*;

import java.util.List;
@Getter
@Setter
public class ProfileUpdateRequest {

    int id;
    String bio;
    String nickname;

}
