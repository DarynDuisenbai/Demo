package com.example.demo.dtos.requests;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DecisionRequest {
    private String IIN;
    private String registrationNumber;
    private String status;
    private String reason;
}
