package com.example.demo.dto.responce;

import com.example.demo.domain.Department;
import com.example.demo.domain.JobTitle;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class UserDto {
    private String email;
    private String password;
    private String name;
    private String secondName;
    private LocalDateTime registrationDate;
    private String profileImage;
    private String IIN;
    private Department department;
    private JobTitle jobTitle;
    private String manager;
    private List<String> conclusions;
    private List<String> tempConclusionDtos;
    private List<String> receivedConclusionDtos;
    private List<AgreementDto> agreementDtos;

}
