package com.example.demo.services.impl;

import com.example.demo.dtos.responces.DepartmentDto;
import com.example.demo.mappers.DepartmentMapper;
import com.example.demo.models.Department;
import com.example.demo.repository.DepartmentRepository;
import com.example.demo.services.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger LOGGER = LoggerFactory.getLogger(DepartmentServiceImpl.class);
    private final DepartmentRepository departmentRepository;
    private final DepartmentMapper departmentMapper;

    @Override
    public Set<DepartmentDto> getAllDepartments() {
        try {
            LOGGER.debug("Retrieving all deps...");
            return departmentRepository.findAll().stream().map(departmentMapper::toDepDto).collect(Collectors.toSet());
        } catch (DataAccessException e) {
            LOGGER.warn("No deps found...");
            return Collections.emptySet();
        }
    }
}
