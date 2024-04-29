package com.example.postservice.repository;

import com.example.plannerentity.dto.responce.PostResponce;
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
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post,Integer> {

    @Query("SELECT p FROM Post p WHERE p.category= :nickname ")
    Page<Post> findAllByNickname(@Param("nickname") String nickname, Pageable pageable);
    @Query("select p from Post p where p.accountId =:id")
    Page<Post> findAllByAccountId(@Param("id") int id,Pageable pageable);
    @Query("select p from Post p where p.accountId =:id")
    List<Post> findAllByAccountId(@Param("id") int id);
    @Query("SELECT p FROM Post p")
    Page<Post> findAllPost(Pageable pageable);
    @Query("SELECT p FROM Post p WHERE p.category= :category ")
    Page<Post> findByCategory(@Param("category") Category category, Pageable pageable);
    @Query("SELECT p FROM Post p WHERE p.name= :name ")
    Page<Post> findAllByName(@Param("name") String name, Pageable pageable);

}
