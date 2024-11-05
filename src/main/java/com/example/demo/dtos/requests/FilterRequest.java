package com.example.demo.dtos.requests;

import com.example.demo.models.Region;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class FilterRequest {
    private String registrationNumber;
    private int status;
    private Region region;
    private LocalDate from;
    private LocalDate to;
    private String IIN;
    private String UD;
    private String fullName;
}
