package com.example.demo.repository;

import com.example.demo.models.Conclusion;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ConclusionRepository extends MongoRepository<Conclusion, String> {
    @Query("{ 'registrationNumber' : ?0, 'documentStatus' : ?1, 'region' : ?2, " +
            " 'creationDate' : { $gte: ?3, $lte: ?4 }, 'iinCalled' : ?5, " +
            " 'udNumber' : ?6, 'agreeingFullName' : ?7 }")
    List<Conclusion> filterConclusions(String registrationNumber, String documentStatus,
                                       String region, LocalDate creationDateFrom,
                                       LocalDate creationDateTo, String iinCalled,
                                       String udNumber, String agreeingFullName);
}
