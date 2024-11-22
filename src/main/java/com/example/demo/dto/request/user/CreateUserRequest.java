package com.example.demo.dto.request.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateUserRequest {
    private String email;
    private String name;
    private String secondName;
    private String password;
    private String IIN;
    private String department;
    private String region;
}
