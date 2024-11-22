package com.example.demo.repository.spec;

import com.example.demo.domain.JobTitle;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface JobRepository extends MongoRepository<JobTitle, String> {
    JobTitle findJobTitleByName(String name);
}
