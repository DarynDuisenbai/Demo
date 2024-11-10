package com.example.demo.dtos.requests;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EditSavedConclusionRequest {
    private String registrationNumber;
    private CreateConclusionRequest createConclusionRequest;
}
