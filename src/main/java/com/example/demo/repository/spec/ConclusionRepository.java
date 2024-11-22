package com.example.demo.repository.spec;

import com.example.demo.domain.Conclusion;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConclusionRepository extends MongoRepository<Conclusion, String>, FilterConclusionRepo {
    Optional<Conclusion> findConclusionByRegistrationNumber(String registrationNumber);
    Optional<Conclusion> findConclusionByIINofCalled(String IINofCalled);

}
