package com.example.demo.repository.spec;

import com.example.demo.domain.Agreement;
import com.example.demo.repository.FilterAgreementRepo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AgreementRepository extends MongoRepository<Agreement, String>, FilterAgreementRepo {

}
