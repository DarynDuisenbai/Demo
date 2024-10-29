package com.example.demo.models;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
@Data
@Document(collection = "conclusions")
public class Conclusion {
    @Id
    private String _id;

    @Field("registrationNumber")
    private String registrationNumber;

    @Field("creationDate")
    private LocalDate creationDate;

    @Field("UD")
    private String UD;

    @Field("registrationDate")
    private LocalDate registrationDate;

    @Field("criminalCode")
    private String criminalCode;

    @Field("solution")
    private String solution;

    @Field("plot")
    private String plot;

    @Field("IINOfCalled")
    private String IINofCalled;

    @Field("fullNameOfCalled")
    private String fullNameOfCalled;

    @Field("jobTitleOfCalled")
    private String jobTitleOfCalled;

    @Field("BIN/IIN")
    private String BINorIIN;

    @Field("jobPlace")
    private String jobPlace;

    @Field("region")
    private String region;

    @Field("plannedActions")
    private String plannedActions;

    @Field("conductTime")
    private LocalDate conductTime;

    @Field("conductPlace")
    private String conductPlace;

    @Field("investigator")
    private String investigator;

    @Field("status")
    private Status status;

    @Field("relation")
    private String relation;

    @Field("investigation")
    private String investigation;

    @Field("isBusiness")
    private boolean isBusiness;

    @Field("BIN/IINPension")
    private String BINOrIINofBusiness;

    @Field("jobPlacePension")
    private String jobPlacePension;

    @Field("IINofDefender")
    private String IINDefender;

    @Field("fullNameOfDefender")
    private String fullNameOfDefender;

    @Field("justification")
    private String justification;

    @Field("result")
    private String result;
}
