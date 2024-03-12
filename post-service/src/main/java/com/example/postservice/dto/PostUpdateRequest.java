package com.example.postservice.dto;

import com.example.plannerentity.enums.Category;
import lombok.Data;

@Data
public class PostUpdateRequest {
    int id;
    String name;
    String content;
    Category category;
}
