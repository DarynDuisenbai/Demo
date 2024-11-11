package com.example.demo.repository;

import com.example.demo.models.JobTitle;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface JobRepository extends MongoRepository<JobTitle, String> {
    JobTitle findJobTitleByName(String name);
}
