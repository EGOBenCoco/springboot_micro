package com.example.postservice.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface S3FileService {

     String uploadFile(MultipartFile file);

    String deleteFileFromS3Bucket(String fileUrl);



    }