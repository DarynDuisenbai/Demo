package com.example.demo.repository.spec;

import com.example.demo.domain.Case;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CaseRepository extends MongoRepository<Case, String> {
    Optional<Case> findCaseByUD(String UD);
}
