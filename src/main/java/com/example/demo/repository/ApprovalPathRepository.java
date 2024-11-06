package com.example.demo.repository;

import com.example.demo.models.ApprovalPath;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApprovalPathRepository extends MongoRepository<ApprovalPath, String> {
}
