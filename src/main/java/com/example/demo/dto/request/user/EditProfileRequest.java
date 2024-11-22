package com.example.demo.dto.request.user;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EditProfileRequest {
    @Size(max = 12, message = "IIN must be at most 12 characters")
    private String IIN;

    @Nullable
    private String name;

    @Nullable
    private String surname;
}
