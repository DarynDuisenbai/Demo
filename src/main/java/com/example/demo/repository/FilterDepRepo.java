package com.example.demo.repository;

import java.util.List;
import java.util.Set;

public interface FilterDepRepo {
    Set<String> findDepartmentsByRegion(String departmentName);
}
