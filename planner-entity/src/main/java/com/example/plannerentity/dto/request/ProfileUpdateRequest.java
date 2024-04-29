package com.example.plannerentity.dto.request;

import com.example.plannerentity.dto.responce.Link;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

public record ProfileUpdateRequest(  int id, String bio, String nickname) {

}
