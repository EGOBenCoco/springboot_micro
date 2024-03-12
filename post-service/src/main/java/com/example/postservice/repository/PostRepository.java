package com.example.postservice.repository;

import com.example.plannerentity.enums.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;
import com.example.postservice.model.Post;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post,Integer> {

    Post findByNickname(@PathVariable String nickname);

    @Query("SELECT p FROM Post p")
    Page<Post> findAllPost(Pageable pageable);
    @Query("SELECT p FROM Post p WHERE p.category= :category ")
    Page<Post> findByCategory(@Param("category") Category category, Pageable pageable);
}
