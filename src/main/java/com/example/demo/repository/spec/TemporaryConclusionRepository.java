package com.example.demo.repository.spec;

import com.example.demo.domain.Conclusion;
import com.example.demo.domain.TemporaryConclusion;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TemporaryConclusionRepository extends MongoRepository<TemporaryConclusion, String> {
    Optional<TemporaryConclusion> findTemporaryConclusionByRegistrationNumber(String number);
    @Query("{ 'registrationNumber': { $in: ?0 } }")
    List<TemporaryConclusion> findByRegistrationNumbers(List<String> registrationNumbers);

}
