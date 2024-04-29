package com.example.plannerentity.dto.request;

import com.example.plannerentity.enums.Category;
import lombok.Data;

public record PostUpdateRequest(   int id,
        String name,
        String content,
        Category category) {

}
