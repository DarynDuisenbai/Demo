package com.example.demo.dtos.requests;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EditProfileRequest {
    private String IIN;

    @Nullable
    private String name;

    @Nullable
    private String surname;

    @Nullable
    @Email(message = "Invalid email format")
    private String email;
}
