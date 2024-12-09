package com.example.demo.service.spec;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;

public interface ImageService {
    String uploadImageToStorage(MultipartFile image) throws IOException;
    Resource getImage(String fileId) throws FileNotFoundException;
}
