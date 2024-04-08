package com.example.plannerentity.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data

public class ProfileUpdateRequest {
    int id;

    String bio;
    String nickname;
   // String auth_id;

}
