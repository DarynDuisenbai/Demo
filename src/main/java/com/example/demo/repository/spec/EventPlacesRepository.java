package com.example.demo.repository.spec;

import com.example.demo.domain.EventPlace;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventPlacesRepository extends MongoRepository<EventPlace, String> {
}
