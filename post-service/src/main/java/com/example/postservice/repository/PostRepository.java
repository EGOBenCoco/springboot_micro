package com.example.postservice.repository;

import com.example.plannerentity.enums.Category;
import com.example.postservice.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post,Integer> {
    @Query("SELECT p FROM Post p WHERE p.author= :author ")
    Page<Post> findAllByAuthor(@Param("author") String author, Pageable pageable);
    @Query("select p from Post p where p.accountId =:id")
    List<Post> findAllByAccountId(@Param("id") int id);
    @Query("SELECT p FROM Post p")
    Page<Post> findAllPost(Pageable pageable);
    @Query("SELECT p FROM Post p WHERE p.category= :category ")
    Page<Post> findByCategory(@Param("category") Category category, Pageable pageable);
    @Query("SELECT p FROM Post p WHERE p.name= :name ")
    Page<Post> findAllByName(@Param("name") String name, Pageable pageable);
}
