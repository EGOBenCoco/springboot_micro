package com.example.plannerentity.dto.request;

import com.example.plannerentity.dto.responce.Link;
import com.example.plannerentity.enums.Category;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ProfileCreatedRequest {
    //int id;
    String bio;
    List<Link> links;
}
