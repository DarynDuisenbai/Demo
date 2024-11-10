package com.example.demo.dtos.responces;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;
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
    private String department;
    private String region;
    private List<ConclusionDto> conclusions;
}
