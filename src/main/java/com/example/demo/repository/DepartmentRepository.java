package com.example.demo.repository;

import com.example.demo.models.Department;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentRepository extends MongoRepository<Department, String> {
    Department findDepartmentByNameAndRegion(String name, String region);

}
