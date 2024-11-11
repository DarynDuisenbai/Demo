package com.example.demo.models;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;

@Getter
@Setter
@Document(collection = "approvalPath")
public class ApprovalPath {

    @Id
    private String _id;

    @Field("jobTitle")
    private String jobTitle;

    @Field("status")
    private Status status;

    @Field("date")
    private LocalDate date;

    @Field("reasonForStatus")
    private String reasonForStatus;
}
