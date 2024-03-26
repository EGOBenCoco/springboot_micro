package com.example.plannerentity.dto.request;

import lombok.Data;

import java.math.BigDecimal;
@Data
public class CountSubscriberMessage {
    private  int profileId;
    private BigDecimal value;
}
