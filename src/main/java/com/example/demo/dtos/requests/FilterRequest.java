package com.example.demo.dtos.requests;

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
    private LocalDateTime from;
    @Nullable
    private LocalDateTime to;
    @Nullable
    private String IIN;
    @Nullable
    private String UD;
    @Nullable
    private String fullName;
}
