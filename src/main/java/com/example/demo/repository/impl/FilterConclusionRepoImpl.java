package com.example.demo.repository.impl;

import com.example.demo.dto.request.conclusion.FilterRequest;
import com.example.demo.domain.Conclusion;
import com.example.demo.repository.FilterConclusionRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

@RequiredArgsConstructor
public class FilterConclusionRepoImpl implements FilterConclusionRepo {

    private final MongoTemplate mongoTemplate;

    @Override
    public List<Conclusion> filterAllConclusions(FilterRequest filterRequest) {
        Query query = new Query();

        if (filterRequest.getRegistrationNumber() != null) {
            query.addCriteria(Criteria.where("registrationNumber").is(filterRequest.getRegistrationNumber()));
        }
        if (filterRequest.getStatus() != null) {
            query.addCriteria(Criteria.where("status.name").is(filterRequest.getStatus()));
        }
        if (filterRequest.getRegion() != null) {
            query.addCriteria(Criteria.where("region.name").is(filterRequest.getRegion()));
        }
        if (filterRequest.getFrom() != null && filterRequest.getTo() != null) {
            query.addCriteria(Criteria.where("creationDate").gte(filterRequest.getFrom()).lte(filterRequest.getTo()));
        }
        if (filterRequest.getIIN() != null) {
            query.addCriteria(Criteria.where("investigator.IIN").is(filterRequest.getIIN()));
        }
        if (filterRequest.getUD() != null) {
            query.addCriteria(Criteria.where("UD").is(filterRequest.getUD()));
        }
        if (filterRequest.getFullName() != null) {
            query.addCriteria(Criteria.where("fullNameOfDefender").is(filterRequest.getFullName()));
        }

        return mongoTemplate.find(query, Conclusion.class);
    }


    @Override
    public List<Conclusion> filterSomeConclusions(List<Conclusion> conclusions, FilterRequest filterRequest) {
        return conclusions.stream()
                .filter(conclusion ->
                        (filterRequest.getRegistrationNumber() == null ||
                                filterRequest.getRegistrationNumber().equals(conclusion.getRegistrationNumber()))
                                &&
                                (filterRequest.getStatus() == null ||
                                        (conclusion.getStatus() != null && filterRequest.getStatus().equals(conclusion.getStatus().getName())))
                                &&
                                (filterRequest.getRegion() == null ||
                                        (conclusion.getRegion() != null && filterRequest.getRegion().equals(conclusion.getRegion().getName())))
                                &&
                                (filterRequest.getFrom() == null || filterRequest.getTo() == null ||
                                        (conclusion.getCreationDate() != null &&
                                                !conclusion.getCreationDate().isBefore(filterRequest.getFrom()) &&
                                                !conclusion.getCreationDate().isAfter(filterRequest.getTo())))
                                &&
                                (filterRequest.getIIN() == null ||
                                        filterRequest.getIIN().equals(conclusion.getInvestigatorIIN()))
                                &&
                                (filterRequest.getUD() == null ||
                                        filterRequest.getUD().equals(conclusion.getUD()))
                                &&
                                (filterRequest.getFullName() == null ||
                                        filterRequest.getFullName().equals(conclusion.getFullNameOfDefender()))
                                &&
                                (filterRequest.getIinOfCalled() == null ||
                                        filterRequest.getIinOfCalled().equals(conclusion.getIINofCalled()))
                ).toList();
    }
}
