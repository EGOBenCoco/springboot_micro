package com.example.profileservice.repository;

import com.example.profileservice.model.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfileRepository extends JpaRepository<Profile,Integer> {

    @Query("select p from Profile p where p.auth_id =:sub")
    Optional<Profile> findProfileByAuth_id(@Param("sub")String sub);


}
