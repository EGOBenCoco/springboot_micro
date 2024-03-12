package com.example.postservice.model;

import com.example.plannerentity.enums.Category;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    int customerId;
    String name;
    String content;
    String nickname;

    @Enumerated(EnumType.STRING)
    Category category;
}
