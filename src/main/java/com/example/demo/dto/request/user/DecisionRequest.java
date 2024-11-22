package com.example.demo.dto.request.user;

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
