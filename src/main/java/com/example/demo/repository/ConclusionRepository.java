package com.example.demo.repository;

import com.example.demo.models.Conclusion;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConclusionRepository extends MongoRepository<Conclusion, String> {
}
