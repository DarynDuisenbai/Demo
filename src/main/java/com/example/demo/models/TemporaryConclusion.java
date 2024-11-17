package com.example.demo.models;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Document(collection = "temp")
public class TemporaryConclusion {
    @Id
    private String _id;

    @Field("registrationNumber")
    @Indexed(unique = true)
    private String registrationNumber;

    @Field("creationDate")
    private LocalDateTime creationDate;

    @Field("UD")
    private String UD;

    @Field("registrationDate")
    private LocalDateTime registrationDate;

    @Field("article")
    private String article;

    @Field("decision")
    private String decision;

    @Field("plot")
    private String plot;

    @Field("IINOfCalled")
    private String IINofCalled;

    @Field("fullNameOfCalled")
    private String fullNameOfCalled;

    @Field("jobTitleOfCalled")
    private String jobTitleOfCalled;

    @Field("BIN/IIN")
    private String BINorIINOfCalled;

    @Field("jobPlace")
    private String jobPlace;

    @Field("region")
    private Region region;

    @Field("plannedActions")
    private String plannedActions;

    @Field("eventTime")
    private LocalDateTime eventTime;

    @Field("eventPlace")
    private String eventPlace;

    @Field("investigator")
    private User investigator;

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

    @Field("IINofDefender")
    private String IINDefender;

    @Field("fullNameOfDefender")
    private String fullNameOfDefender;

    @Field("justification")
    private String justification;

    @Field("result")
    private String result;

}
