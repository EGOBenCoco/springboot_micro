package com.example.postservice.service.impl;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
class S3FileServiceImpTest {
    @Mock
    private AmazonS3 amazonS3;

    @Value("${s3.bucketName}")
    private String bucketName;

    @InjectMocks
    private S3FileServiceImp s3FileServiceImpl;



    @Test
    void testUploadFile() throws IOException {
        MultipartFile multipartFile = mock(MultipartFile.class);
        when(multipartFile.getOriginalFilename()).thenReturn("test.jpg");
        when(multipartFile.getBytes()).thenReturn(new byte[]{1, 2, 3});

        String fileUrl = s3FileServiceImpl.uploadFile(multipartFile);

        verify(amazonS3, times(1)).putObject(any(PutObjectRequest.class));
        assertNotNull(fileUrl);
        assertTrue(fileUrl.endsWith("test.jpg"));
    }

    @Test
    void testDeleteFileFromS3Bucket() {
        String fileUrl = "test.jpg";

        String result = s3FileServiceImpl.deleteFileFromS3Bucket(fileUrl);

        verify(amazonS3, times(1)).deleteObject(bucketName, fileUrl);
        assertEquals(" removed ...", result);
    }
}