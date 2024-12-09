package com.example.demo.util;

import com.example.demo.domain.Conclusion;
import com.example.demo.domain.TemporaryConclusion;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.text.RandomStringGenerator;
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
    public String generatePassword() {
        String uppercase = new RandomStringGenerator.Builder()
                .withinRange('A', 'Z')
                .build()
                .generate(1);

        String lowercase = new RandomStringGenerator.Builder()
                .withinRange('a', 'z')
                .build()
                .generate(1);

        String digit = new RandomStringGenerator.Builder()
                .withinRange('0', '9')
                .build()
                .generate(1);

        String specialChar = new RandomStringGenerator.Builder()
                .withinRange('!', '?')
                .filteredBy(ch -> "!@#$%^&*()-+=<>?".indexOf(ch) >= 0)
                .build()
                .generate(1);

        String remaining = new RandomStringGenerator.Builder()
                .withinRange('0', 'z')
                .filteredBy(Character::isLetterOrDigit)
                .build()
                .generate(3);

        String password = uppercase + lowercase + digit + specialChar + remaining;
        return new String(new java.util.Random().ints(0, password.length())
                .distinct()
                .limit(password.length())
                .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
                .toString());
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
                throw new IOException("File not found in resources: jobPlaces.json");
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

    public String generateWorkPlaceBusiness(){
        ObjectMapper objectMapper = new ObjectMapper();
        Random random = new Random();

        try {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("pension.json");
            if (inputStream == null) {
                throw new IOException("File not found in resources: pension.json");
            }

            List<String> workPlaceBusiness = objectMapper.readValue(
                    inputStream,
                    new TypeReference<>() {
                    }
            );
            return workPlaceBusiness.get(random.nextInt(workPlaceBusiness.size()));

        } catch (IOException e) {
            e.printStackTrace();
            return "Error: Unable to generate names.";
        }
    }

    public String generateBIN() {
        Random random = new Random();
        StringBuilder binBuilder = new StringBuilder();

        for (int i = 0; i < 11; i++) {
            int digit = random.nextInt(10);
            binBuilder.append(digit);
        }

        int checksum = calculateLuhnChecksum(binBuilder.toString());

        binBuilder.append(checksum);

        return binBuilder.toString();
    }

    private int calculateLuhnChecksum(String digits) {
        int sum = 0;
        boolean alternate = false;

        for (int i = digits.length() - 1; i >= 0; i--) {
            int digit = Character.getNumericValue(digits.charAt(i));

            if (alternate) {
                digit *= 2;
                if (digit > 9) {
                    digit -= 9;
                }
            }

            sum += digit;
            alternate = !alternate;
        }

        return (10 - (sum % 10)) % 10;
    }

}
