package com.example.subscriberservice.repository;

import com.example.subscriberservice.model.Subscriber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubscriberRepository  extends JpaRepository<Subscriber,Integer> {

    List<Subscriber> findAllByProfileId(int id);
    List<Subscriber> findAllById(int id);

    Subscriber findBySubscriberIdAndProfileId(int profileId,int subscriberId);
}
