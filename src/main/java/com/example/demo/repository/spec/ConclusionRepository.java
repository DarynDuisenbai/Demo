package com.example.demo.repository.spec;

import com.example.demo.domain.Conclusion;
import com.example.demo.repository.FilterConclusionRepo;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConclusionRepository extends MongoRepository<Conclusion, String>, FilterConclusionRepo {
    Optional<Conclusion> findConclusionByRegistrationNumber(String registrationNumber);
    @Aggregation(pipeline = {
            "{ $match: { registrationNumber: { $in: ?0 } } }",
            "{ $addFields: { sortOrder: { $switch: { branches: [" +
                    "{ case: { $eq: ['$status.name', 'На согласовании'] }, then: 1 }," +
                    "{ case: { $eq: ['$status.name', 'Отправлено на доработку'] }, then: 2 }," +
                    "{ case: { $eq: ['$status.name', 'Отказано'] }, then: 3 }," +
                    "{ case: { $eq: ['$status.name', 'Оставлено без рассмотрения'] }, then: 4 }," +
                    "{ case: { $eq: ['$status.name', 'Согласовано'] }, then: 5 }" +
                    "], default: 6 } } } }",
            "{ $sort: { sortOrder: 1 } }"
    })
    List<Conclusion> findByRegistrationNumbers(List<String> registrationNumbers);

}
