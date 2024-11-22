package com.example.demo.dto.responce;

import com.example.demo.domain.Department;
import com.example.demo.domain.JobTitle;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TokenInfo {
    private String name;
    private String secondName;
    private String email;
    private String profileImage;
    private String registrationDate;
    private Department department;
    private String IIN;
    private JobTitle job;
}
