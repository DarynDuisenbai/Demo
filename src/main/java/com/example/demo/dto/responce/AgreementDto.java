package com.example.demo.dto.responce;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class AgreementDto {
    private String registrationNumber;
    private String jobTitle;
    private String fullName;
    private String status;
    private LocalDateTime date;
    private String reason;
}
