package com.example.demo.service.impl;

import com.example.demo.service.spec.ImageService;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.model.GridFSFile;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.bson.types.ObjectId;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

    private final GridFsTemplate gridFsTemplate;
    private final GridFSBucket gridFSBucket;

    public String uploadImageToStorage(MultipartFile image) throws IOException {
        String fileName = StringUtils.cleanPath(image.getOriginalFilename());

        ObjectId fileId = gridFsTemplate.store(image.getInputStream(), fileName, MediaType.IMAGE_JPEG_VALUE);

        return fileId.toString();
    }

    public Resource getImage(String fileId) throws FileNotFoundException {
        GridFSFile file = gridFsTemplate.findOne(Query.query(Criteria.where("_id").is(fileId)));
        if (file != null) {
            return new InputStreamResource(gridFSBucket.openDownloadStream(file.getId()));
        }
        throw new FileNotFoundException("File not found with ID: " + fileId);
    }
}
