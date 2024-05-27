package com.example.postservice.service.impl;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.util.IOUtils;
import com.example.postservice.service.S3FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;


@Service
@RequiredArgsConstructor

public class S3FileServiceImp implements S3FileService {

    private final AmazonS3 amazonS3;

    @Value("${s3.bucketName}")
    private String bucketName;



    @Override
    public String uploadFile(MultipartFile file) {
        File fileObj = convertMultiPartToFile(file);
        String fileName = System.currentTimeMillis() +  file.getOriginalFilename();
        amazonS3.putObject(new PutObjectRequest(bucketName, fileName, fileObj));
        fileObj.delete();
        return fileName;
    }


    private File convertMultiPartToFile(MultipartFile file) {
        File convertedFile = new File(file.getOriginalFilename());
        try (FileOutputStream fos = new FileOutputStream(convertedFile)) {
            fos.write(file.getBytes());

        } catch (IOException e) {
            e.printStackTrace();
        }
        return convertedFile;
    }


    public String deleteFileFromS3Bucket(String fileUrl) {
        amazonS3.deleteObject(bucketName, fileUrl);
        return  " removed ...";
    }


}