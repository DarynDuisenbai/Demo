package com.example.demo.services.impl;

import com.example.demo.dtos.responces.DepartmentDto;
import com.example.demo.mappers.DepartmentMapper;
import com.example.demo.models.Department;
import com.example.demo.repository.DepartmentRepository;
import com.example.demo.services.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {
    private final DepartmentRepository departmentRepository;
    private final DepartmentMapper departmentMapper;

    @Override
    public Set<DepartmentDto> getAllDepartments() {
        try {
            return departmentRepository.findAll().stream().map(departmentMapper::toDepDto).collect(Collectors.toSet());
        } catch (DataAccessException e) {
            return Collections.emptySet();
        }
    }
}
