package com.example.demo.repository;

import com.example.demo.models.Agreement;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.OptionalInt;

@Repository
public interface AgreementRepository extends MongoRepository<Agreement, String>, FilterAgreementRepo {

}
