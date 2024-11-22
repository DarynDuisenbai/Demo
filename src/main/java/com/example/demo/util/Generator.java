package com.example.demo.util;

import com.example.demo.domain.Conclusion;
import com.example.demo.domain.TemporaryConclusion;
import lombok.RequiredArgsConstructor;
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
