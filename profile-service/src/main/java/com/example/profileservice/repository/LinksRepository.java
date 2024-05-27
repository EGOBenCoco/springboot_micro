package com.example.profileservice.repository;

import com.example.profileservice.model.Link;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LinksRepository extends JpaRepository<Link,Integer> {
}
