package com.example.demo.services;

import com.example.demo.dtos.responces.DepartmentDto;

import java.util.Set;

public interface DepartmentService {
    Set<DepartmentDto> getAllDepartments();
}
