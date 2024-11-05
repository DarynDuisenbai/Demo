package com.example.demo.mappers;

import com.example.demo.dtos.responces.DepartmentDto;
import com.example.demo.models.Department;

public interface DepartmentMapper {
    DepartmentDto toDepDto(Department department);
}
