package com.example.profileservice.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.ColumnTransformer;

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
    @ColumnTransformer(write = "LOWER(?)")
    String nickname;
    @Size(min = 8, max = 10,message = "The size should vary from 8 to 100")
    String bio;
    String auth_id;
    BigDecimal countSubscriber;

    @OneToMany(mappedBy = "profile",cascade = {CascadeType.PERSIST,CascadeType.REMOVE},fetch = FetchType.EAGER)
    List<Link> links;
    public Profile() {
        this.countSubscriber = BigDecimal.ZERO;
    }
}
