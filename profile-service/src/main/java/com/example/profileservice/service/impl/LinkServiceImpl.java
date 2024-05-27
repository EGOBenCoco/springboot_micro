package com.example.profileservice.service.impl;

import com.example.profileservice.model.Link;
import com.example.profileservice.repository.LinksRepository;
import com.example.profileservice.service.LinkService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class LinkServiceImpl implements LinkService {

    LinksRepository linksRepository;
    @Override
    public void createLink(Link link) {
        linksRepository.save(link);
    }

    @Override
    public void updateLink(Link link) {
        linksRepository.save(link);
    }

    @Override
    public void deleteLink(int linkId) {
        linksRepository.deleteById(linkId);
    }
}
