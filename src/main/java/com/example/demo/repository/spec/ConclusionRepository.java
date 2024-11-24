package com.example.demo.repository.spec;

import com.example.demo.domain.Conclusion;
import com.example.demo.repository.FilterConclusionRepo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConclusionRepository extends MongoRepository<Conclusion, String>, FilterConclusionRepo {
    Optional<Conclusion> findConclusionByRegistrationNumber(String registrationNumber);
    @Query("{ 'registrationNumber': { $in: ?0 } }")
    List<Conclusion> findByRegistrationNumbers(List<String> registrationNumbers);

}
