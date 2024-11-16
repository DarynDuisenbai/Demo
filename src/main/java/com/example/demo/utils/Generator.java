package com.example.demo.utils;

import com.example.demo.models.Conclusion;
import com.example.demo.models.TemporaryConclusion;
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
        long countConc = mongoTemplate.count(new Query(), Conclusion.class);
        long countTemp = mongoTemplate.count(new Query(), TemporaryConclusion.class);
        return PREFIX + String.format("%03d", countConc + countTemp + 1);
    }
}
