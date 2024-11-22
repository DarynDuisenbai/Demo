package com.example.demo.repository.spec;

import com.example.demo.domain.TemporaryConclusion;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TemporaryConclusionRepository extends MongoRepository<TemporaryConclusion, String> {
    Optional<TemporaryConclusion> findTemporaryConclusionByRegistrationNumber(String number);

}
