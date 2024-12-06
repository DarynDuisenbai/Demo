package com.example.demo.util;

import com.example.demo.domain.Conclusion;
import com.example.demo.domain.TemporaryConclusion;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.List;
import java.util.Random;

@Component
@RequiredArgsConstructor
public class Generator {
    private final String PREFIX = "Z";
    private final MongoTemplate mongoTemplate;

    public String generateUniqueNumber() {
        int maxFromConclusions = findMaxNumberFromCollection(Conclusion.class);
        int maxFromTemporary = findMaxNumberFromCollection(TemporaryConclusion.class);

        int nextNumber = Math.max(maxFromConclusions, maxFromTemporary) + 1;
        return PREFIX + String.format("%03d", nextNumber);
    }

    private <T> int findMaxNumberFromCollection(Class<T> entityClass) {
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(Criteria.where("registrationNumber").regex("^" + PREFIX + "\\d+$")),
                Aggregation.project()
                        .andExpression("substr(registrationNumber, 1, -1)").as("numericPart"),
                Aggregation.project()
                        .andExpression("toInt(numericPart)").as("numericPartInt"),
                Aggregation.group().max("numericPartInt").as("maxNumber")
        );

        AggregationResults<Document> results = mongoTemplate.aggregate(aggregation, entityClass, Document.class);

        Document maxNumberResult = results.getUniqueMappedResult();
        return maxNumberResult != null ? maxNumberResult.getInteger("maxNumber", 0) : 0;
    }

    public String generateNames(){
        ObjectMapper objectMapper = new ObjectMapper();
        Random random = new Random();

        try {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("names.json");
            if (inputStream == null) {
                throw new IOException("File not found in resources: names.json");
            }

            List<String> names = objectMapper.readValue(
                    inputStream,
                    new TypeReference<>() {
                    }
            );
            return names.get(random.nextInt(names.size()));

        } catch (IOException e) {
            e.printStackTrace();
            return "Error: Unable to generate names.";
        }
    }
    public String generateJobPlaces(){
        ObjectMapper objectMapper = new ObjectMapper();
        Random random = new Random();

        try {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("jobPlaces.json");
            if (inputStream == null) {
                throw new IOException("File not found in resources: names.json");
            }

            List<String> jobPlaces = objectMapper.readValue(
                    inputStream,
                    new TypeReference<>() {
                    }
            );
            return jobPlaces.get(random.nextInt(jobPlaces.size()));

        } catch (IOException e) {
            e.printStackTrace();
            return "Error: Unable to generate names.";
        }
    }
}
