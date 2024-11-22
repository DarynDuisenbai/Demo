package com.example.demo.service.impl;

import com.example.demo.domain.Department;
import com.example.demo.repository.spec.DepartmentRepository;
import com.example.demo.service.spec.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
