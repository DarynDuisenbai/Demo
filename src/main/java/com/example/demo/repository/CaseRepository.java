package com.example.demo.repository;

import com.example.demo.models.Case;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CaseRepository extends MongoRepository<Case, String> {
    Optional<Case> findCaseByUD(String UD);
}
