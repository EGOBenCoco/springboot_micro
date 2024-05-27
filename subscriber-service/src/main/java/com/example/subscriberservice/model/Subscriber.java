package com.example.subscriberservice.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Subscriber {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "subscriber_profile_ids", joinColumns = @JoinColumn(name = "subscriber_id"))
    @Column(name = "profile_id")
    List<Integer> profileId = new ArrayList<>();

    String subscriberEmail;


}
