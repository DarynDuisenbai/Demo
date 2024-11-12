package com.example.demo.models;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;

@Getter
@Setter
public class ApprovalPath {

    private String _id;

    private String jobTitle;

    private Status status;

    private LocalDate date;

    private String reasonForStatus;
}
