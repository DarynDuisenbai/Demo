package com.example.demo.dto.request.conclusion;

import com.example.demo.dto.request.conclusion.CreateConclusionRequest;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EditSavedConclusionRequest {
    private String registrationNumber;
    private CreateConclusionRequest createConclusionRequest;
}
