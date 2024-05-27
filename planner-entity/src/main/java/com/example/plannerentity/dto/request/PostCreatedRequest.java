package com.example.plannerentity.dto.request;

import com.example.plannerentity.enums.Category;
import lombok.*;

@Getter
@Setter
public class PostCreatedRequest {

   // int accountId;
    String name;
    String content;
    Category category;

}
