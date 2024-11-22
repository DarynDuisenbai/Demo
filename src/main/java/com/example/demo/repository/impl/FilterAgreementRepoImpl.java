package com.example.demo.repository.impl;

import com.example.demo.domain.Agreement;
import com.example.demo.repository.spec.FilterAgreementRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class FilterAgreementRepoImpl implements FilterAgreementRepo {

    private final MongoTemplate mongoTemplate;
    @Override
    public List<Agreement> getAgreementsByIInOfCalled(String IINofCalled) {
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(Criteria.where("IINOfCalled").is(IINofCalled)),
                Aggregation.lookup("agreement", "registrationNumber", "registrationNumber", "matchedAgreements"),
                Aggregation.unwind("matchedAgreements"),
                Aggregation.replaceRoot("matchedAgreements")
        );
        AggregationResults<Agreement> results = mongoTemplate.aggregate(aggregation, "conclusions", Agreement.class);

        return results.getMappedResults();
    }
}
