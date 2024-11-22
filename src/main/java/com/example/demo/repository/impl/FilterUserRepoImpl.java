package com.example.demo.repository.impl;

import com.example.demo.domain.User;
import com.example.demo.repository.spec.FilterUserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

@RequiredArgsConstructor
public class FilterUserRepoImpl implements FilterUserRepo {
    private final MongoTemplate mongoTemplate;

    @Override
    public List<User> findByDepartment(String department) {
        Query query = new Query();
        query.addCriteria(Criteria.where("department.name").is(department));

        return mongoTemplate.find(query, User.class);
    }

    @Override
    public List<User> findByJob(String job) {
        Query query = new Query();
        query.addCriteria(Criteria.where("jobTitle.name").is(job));

        return mongoTemplate.find(query, User.class);
    }

    @Override
    public User findAnalystByDepartment(String department) {
        Query query = new Query();
        query.addCriteria(Criteria.where("jobTitle.name").is("Аналитик СД"));
        query.addCriteria(Criteria.where("department.name").is(department));

        return mongoTemplate.findOne(query, User.class);
    }
}
