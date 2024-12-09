package com.example.demo.service.spec;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ImageService {
    String uploadImageToStorage(MultipartFile image) throws IOException;
}
