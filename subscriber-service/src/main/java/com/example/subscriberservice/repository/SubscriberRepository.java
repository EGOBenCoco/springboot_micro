package com.example.subscriberservice.repository;
import org.springframework.data.domain.Page;

import com.example.subscriberservice.model.Subscriber;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubscriberRepository  extends JpaRepository<Subscriber,Integer> {
    @Query("SELECT s FROM Subscriber s WHERE :id MEMBER OF s.profileId")
    List<Subscriber> findAllByProfileId(int id);
    Page<Subscriber> findById(int subscriberId, Pageable pageable);
    Optional<Subscriber> findBySubscriberEmail(String email);
    @Query("SELECT CASE WHEN COUNT(e) > 0 THEN true ELSE false END FROM Subscriber e WHERE e.subscriberEmail = :subscriberEmail AND e.profileId = :profileId")
    boolean existsBySubscriberEmailAndProfileId(String subscriberEmail, Integer profileId);
}
