package com.example.demo.util;

import com.example.demo.domain.Conclusion;
import com.example.demo.domain.TemporaryConclusion;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Random;

@Component
@RequiredArgsConstructor
public class Generator {
    private final String PREFIX = "Z";
    private final MongoTemplate mongoTemplate;
    private final String NAMES_PATH = "src/main/resources/names.json";

    public String generateUniqueNumber(){
        long countConc = mongoTemplate.count(new Query(), Conclusion.class);
        long countTemp = mongoTemplate.count(new Query(), TemporaryConclusion.class);
        return PREFIX + String.format("%03d", countConc + countTemp + 1);
    }

    public String generateNames(){
        ObjectMapper objectMapper = new ObjectMapper();
        Random random = new Random();

        try {
            List<String> names = objectMapper.readValue(
                    Paths.get(NAMES_PATH).toFile(),
                    new TypeReference<>() {
                    }
            );
            return names.get(random.nextInt(names.size()));

        } catch (IOException e) {
            e.printStackTrace();
            return "Error: Unable to generate names.";
        }
    }
}
