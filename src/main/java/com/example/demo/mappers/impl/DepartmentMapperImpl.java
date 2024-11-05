package com.example.demo.mappers.impl;

import com.example.demo.dtos.responces.DepartmentDto;
import com.example.demo.mappers.DepartmentMapper;
import com.example.demo.models.Department;
import org.springframework.stereotype.Component;


@Component
public class DepartmentMapperImpl implements DepartmentMapper {
    @Override
    public DepartmentDto toDepDto(Department department) {
        DepartmentDto departmentDto = new DepartmentDto();
        departmentDto.setName(department.getName());
        departmentDto.setRegion(department.getRegion());

        return departmentDto;
    }
}
