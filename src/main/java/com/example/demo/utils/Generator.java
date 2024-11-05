package com.example.demo.utils;

import com.example.demo.models.Conclusion;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Generator {
    private final String PREFIX = "Z";
    private final MongoTemplate mongoTemplate;

    public String generateUniqueNumber(){
        long count = mongoTemplate.count(new Query(), Conclusion.class);
        return PREFIX + String.format("%03d", count + 1);
    }
}
