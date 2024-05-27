package com.example.profileservice.service;

import com.example.profileservice.model.Link;

public interface LinkService {
    void createLink(Link link);
    void updateLink(Link link);
    void deleteLink(int id);
}
