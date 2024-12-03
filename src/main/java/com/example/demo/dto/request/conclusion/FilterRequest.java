package com.example.demo.dto.request.conclusion;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.Nullable;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class FilterRequest {
    @Nullable
    private String registrationNumber;
    @Nullable
    private String status;
    @Nullable
    private String region;
    @Nullable
    private LocalDate from;
    @Nullable
    private LocalDate to;

    @NotNull
    private String IIN;
    @Nullable
    private String UD;
    @Nullable
    private String fullName;
    @Nullable
    private String iinOfCalled;
}
