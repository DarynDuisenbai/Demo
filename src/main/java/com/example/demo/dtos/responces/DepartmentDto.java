package com.example.demo.dtos.responces;

import com.example.demo.models.Region;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DepartmentDto {
    private String name;
    private Region region;
}
