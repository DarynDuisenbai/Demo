package com.example.demo.repository;

import com.example.demo.models.Status;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StatusRepository extends MongoRepository<Status,String> {
    Status findByName(String name);
}
