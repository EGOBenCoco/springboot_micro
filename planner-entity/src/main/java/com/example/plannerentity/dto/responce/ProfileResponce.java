package com.example.plannerentity.dto.responce;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProfileResponce {

    int id;
    String auth_id;
    String nickname;
    String bio;
    BigDecimal countSubscriber;
    List<Link> links;
}
