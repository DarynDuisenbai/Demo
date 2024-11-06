package com.example.demo.repository;

import com.example.demo.models.Conclusion;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ConclusionRepository extends MongoRepository<Conclusion, String>, FilterRepo {
}
