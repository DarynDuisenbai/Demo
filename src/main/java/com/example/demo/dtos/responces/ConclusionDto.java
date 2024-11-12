package com.example.demo.dtos.responces;

import com.example.demo.models.Region;
import com.example.demo.models.Status;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
public class ConclusionDto {
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
    private String investigator;
    private String status;
    private String relationToEvent;
    private String investigationTypes;
    private boolean relatesToBusiness;
    private String defenseAttorneyIIN;
    private String defenseAttorneyFullName;
    private String justification;
    private String result;
}
