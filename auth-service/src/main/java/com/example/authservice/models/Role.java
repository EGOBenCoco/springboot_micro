package com.example.authservice.models;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Objects;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Role {

    int id;


    EnumRole name;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Role otherRole = (Role) obj;
        return Objects.equals(id, otherRole.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}