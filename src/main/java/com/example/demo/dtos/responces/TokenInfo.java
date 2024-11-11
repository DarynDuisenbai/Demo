package com.example.demo.dtos.responces;

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
    private String department;
    private String IIN;
    private String job;
    private String role;
}
