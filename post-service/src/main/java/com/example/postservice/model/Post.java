package com.example.postservice.model;

import com.example.plannerentity.enums.Category;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.ColumnTransformer;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;


    int profileId;

    @NotEmpty(message = "Name is required")
    String name;
    @NotEmpty(message = "Content is required")
    String content;
    @ColumnTransformer(write = "LOWER(?)")
    String author;
    //@NotEmpty(message = "Category is required")
    @Enumerated(EnumType.STRING)
    Category category;

    @CreationTimestamp
    @Column(name = "created_at")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Temporal(TemporalType.TIMESTAMP)
    private ZonedDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Temporal(TemporalType.TIMESTAMP)
    private ZonedDateTime updatedAt;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "post_photos_url", joinColumns = @JoinColumn(name = "post_id"))
    @Column(name = "photo_url")
    @Builder.Default
    List<String> photoUrls = new ArrayList<>();
}
