package com.example.postservice.dto;

import com.example.plannerentity.enums.Category;
import com.example.postservice.model.Post;
import lombok.Data;

@Data
public class PostResponce {

    int id;
    String name;
    String content;
    Category category;

    public PostResponce(Post post){
        this.id = post.getId();
        this.name = post.getName();
        this.content = post.getContent();
        this.category = post.getCategory();
    }

}
