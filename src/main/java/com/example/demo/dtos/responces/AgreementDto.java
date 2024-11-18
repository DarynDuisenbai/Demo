package com.example.demo.dtos.responces;

import com.example.demo.models.Status;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class AgreementDto {
    private String jobTitle;
    private String fullName;
    private String status;
    private LocalDateTime date;
    private String reason;
}
