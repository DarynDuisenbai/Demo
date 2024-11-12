package com.example.demo.dtos.requests;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class CreateConclusionRequest {
    private String UD;
    private String IIN;
    private String BIN_IIN;
    private String jobTitle;
    private String region;
    private String plannedActions;
    private LocalDateTime eventDateTime;
    private String eventPlace;
    private String relation;
    private String investigationType;
    private Boolean relatesToBusiness;
    private String IINDefender;
    private String justification;
    private String result;
}
