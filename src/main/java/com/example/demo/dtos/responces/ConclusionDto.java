package com.example.demo.dtos.responces;

import com.example.demo.models.Region;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
public class ConclusionDto {
    private String id;
    private String registrationNumber;
    private String creationDate;
    private String udNumber;
    private LocalDate registrationDate;
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
    private LocalDate eventDateTime;
    private String eventPlace;
    private String investigator;
    private int status;
    private String relationToEvent;
    private String investigationTypes;
    private boolean relatesToBusiness;
    private String defenseAttorneyIIN;
    private String defenseAttorneyFullName;
    private String justification;
    private String result;
}
