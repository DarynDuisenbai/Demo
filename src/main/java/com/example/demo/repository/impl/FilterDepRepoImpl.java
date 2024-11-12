package com.example.demo.repository.impl;

import com.example.demo.models.Department;
import com.example.demo.repository.FilterDepRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class FilterDepRepoImpl implements FilterDepRepo {

    private final MongoTemplate mongoTemplate;

    @Override
    public Set<String> findDepartmentsByRegion(String departmentName) {
        Query query = new Query();
        query.addCriteria(Criteria.where("name").is(departmentName));

        return mongoTemplate.find(query, Department.class)
                .stream()
                .map(Department::getRegion)
                .collect(Collectors.toSet());
    }
}
