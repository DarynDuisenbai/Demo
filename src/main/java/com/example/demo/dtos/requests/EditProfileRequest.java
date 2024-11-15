package com.example.demo.dtos.requests;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EditProfileRequest {
    private String IIN;
    private String name;
    private String surname;
    private String email;
}
