package com.example.demo.services.impl;

import com.example.demo.models.Department;
import com.example.demo.repository.DepartmentRepository;
import com.example.demo.services.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;

    @Override
    public Set<String> getAllDepartments() {
       return departmentRepository.findAll().stream().map(Department::getName).collect(Collectors.toSet());
    }
}
