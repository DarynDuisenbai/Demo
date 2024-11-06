package com.example.demo.dtos.requests;

import com.example.demo.models.Region;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class CreateConclusionRequest {
    private String UD;
    private String IIN;
    private String BIN_IIN;
    private String jobTitle;
    private Region region;
    private String plannedActions;
    private LocalDate eventDateTime;
    private String eventPlace;
   // private int status;
    private String relation;
    private String investigationType;
    private Boolean relatesToBusiness;
    private String IINDefender;
    private String justification;
    private String result;
}
