package com.example.demo.service.spec;

import java.util.Set;

public interface DepartmentService {
    Set<String> getAllDepartments();
    Set<String> allRegionsInDep(String dep);
}
