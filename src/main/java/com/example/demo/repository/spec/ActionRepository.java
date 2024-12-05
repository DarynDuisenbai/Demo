package com.example.demo.repository.spec;

import com.example.demo.domain.Action;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActionRepository extends MongoRepository<Action, String> {
}

