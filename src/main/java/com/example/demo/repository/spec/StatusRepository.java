package com.example.demo.repository.spec;

import com.example.demo.domain.Status;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StatusRepository extends MongoRepository<Status,String> {
    Status findByName(String name);
}
