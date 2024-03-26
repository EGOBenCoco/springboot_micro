package com.example.plannerentity.dto.responce;

import com.example.plannerentity.enums.Category;
import lombok.Data;

@Data
public class PostResponce {

    int id;
    String name;
    String content;
    Category category;
    String nickname;
}
