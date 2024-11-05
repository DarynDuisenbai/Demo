package com.example.demo.dtos.responces;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class UserDto {
    private String email;
    private String password;
    private String name;
    private String secondName;
    private Date registrationDate;
    private String profileImage;
}
