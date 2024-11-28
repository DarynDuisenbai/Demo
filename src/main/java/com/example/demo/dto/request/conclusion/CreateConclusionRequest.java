package com.example.demo.dto.request.conclusion;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CreateConclusionRequest {
    private String UD;
    private String IINOfCalled;
    private String BIN_IIN;
    private String jobTitle;
    private String region;
    private String plannedActions;
    private LocalDateTime eventDateTime;
    private String eventPlace;
    private String relation;
    private String investigationType;
    private String relatesToBusiness;
    private String IINOfInvestigator;
    private String IINDefender;
    private String justification;
    private String result;
}
