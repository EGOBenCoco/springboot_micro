package com.example.profileservice.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    String nickname;
    String bio;
    String auth_id;
    BigDecimal countSubscriber;

    @OneToMany(mappedBy = "profile",cascade = {CascadeType.PERSIST,CascadeType.REMOVE},fetch = FetchType.EAGER)
    List<Link> links;
    public Profile() {
        this.countSubscriber = BigDecimal.ZERO;
    }
}
