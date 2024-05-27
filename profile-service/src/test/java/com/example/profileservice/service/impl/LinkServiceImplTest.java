package com.example.profileservice.service.impl;

import com.example.profileservice.model.Link;
import com.example.profileservice.repository.LinksRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class LinkServiceImplTest {

    @Mock
    private LinksRepository linksRepository;

    @InjectMocks
    private LinkServiceImpl linkServiceImpl;

    private Link link;

    @BeforeEach
    void setUp() {
        link = new Link();
        link.setId(1);
        link.setValue("https://example.com");
        link.setName("Test link");
    }

    @Test
    void testCreateLink() {
        linkServiceImpl.createLink(link);

        verify(linksRepository, times(1)).save(link);
    }

    @Test
    void testUpdateLink() {
        linkServiceImpl.updateLink(link);

        verify(linksRepository, times(1)).save(link);
    }

    @Test
    void testDeleteLink() {
        linkServiceImpl.deleteLink(link.getId());

        verify(linksRepository, times(1)).deleteById(link.getId());
    }
}