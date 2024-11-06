package com.example.demo.repository;

import com.example.demo.dtos.requests.FilterRequest;
import com.example.demo.models.Conclusion;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
public class FilterRepoImpl implements FilterRepo{

    private final MongoTemplate mongoTemplate;

    @Override
    public List<Conclusion> filterConclusions(FilterRequest filterRequest) {
        Query query = new Query();

        if (filterRequest.getRegistrationNumber() != null) {
            query.addCriteria(Criteria.where("registrationNumber").is(filterRequest.getRegistrationNumber()));
        }
        if (filterRequest.getStatus() != 0) {
            query.addCriteria(Criteria.where("status").is(filterRequest.getStatus()));
        }
        if (filterRequest.getRegion() != null) {
            query.addCriteria(Criteria.where("region").is(filterRequest.getRegion()));
        }
        if (filterRequest.getFrom() != null && filterRequest.getTo() != null) {
            query.addCriteria(Criteria.where("creationDate").gte(filterRequest.getFrom()).lte(filterRequest.getTo()));
        }
        if (filterRequest.getIIN() != null) {
            query.addCriteria(Criteria.where("IINOfCalled").is(filterRequest.getIIN()));
        }
        if (filterRequest.getUD() != null) {
            query.addCriteria(Criteria.where("UD").is(filterRequest.getUD()));
        }
        if (filterRequest.getFullName() != null) {
            query.addCriteria(Criteria.where("fullNameOfDefender").is(filterRequest.getFullName()));
        }

        return mongoTemplate.find(query, Conclusion.class);
    }
}
