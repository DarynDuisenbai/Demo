package com.example.demo.dtos.responces;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class AgreementDto {
    private String jobTitle;
    private String fullName;
    private String status;
    private LocalDate date;
    private String reason;
}
