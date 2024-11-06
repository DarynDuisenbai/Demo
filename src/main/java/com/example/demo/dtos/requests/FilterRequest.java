package com.example.demo.dtos.requests;

import com.example.demo.models.Region;
import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.Nullable;

import java.time.LocalDate;

@Getter
@Setter
public class FilterRequest {
    @Nullable
    private String registrationNumber;
    @Nullable
    private Integer status;
    @Nullable
    private Region region;
    @Nullable
    private LocalDate from;
    @Nullable
    private LocalDate to;
    @Nullable
    private String IIN;
    @Nullable
    private String UD;
    @Nullable
    private String fullName;
}
