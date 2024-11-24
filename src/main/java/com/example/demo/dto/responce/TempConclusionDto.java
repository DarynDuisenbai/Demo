package com.example.demo.dto.responce;

import com.example.demo.domain.Region;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class TempConclusionDto {
    private String registrationNumber;
    private LocalDateTime creationDate;
    private String udNumber;
    private LocalDateTime registrationDate;
    private String article;
    private String decision;
    private String summary;
    private String calledPersonIIN;
    private String calledPersonFullName;
    private String calledPersonPosition;
    private String calledPersonBIN;
    private String workPlace;
    private Region region;
    private String plannedInvestigativeActions;
    private LocalDateTime eventDateTime;
    private String eventPlace;
    private String investigatorIIN;
    private String status;
    private String relationToEvent;
    private String investigationTypes;
    private boolean relatesToBusiness;
    private String defenseAttorneyIIN;
    private String defenseAttorneyFullName;
    private String justification;
    private String result;
}
