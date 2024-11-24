package com.example.demo.repository.spec;

import com.example.demo.domain.Department;
import com.example.demo.repository.FilterDepRepo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentRepository extends MongoRepository<Department, String>, FilterDepRepo {
    Department findDepartmentByNameAndRegion(String name, String region);
}
