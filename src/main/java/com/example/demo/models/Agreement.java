package com.example.demo.models;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Document(collection = "agreement")
public class Agreement {

    @Id
    private String _id;

    @Field("registrationNumber")
    private String regNumber;

    @Field("fullname")
    private String fullName;

    @Field("job")
    private String jobTitle;

    @Field("status")
    private Status status;

    @Field("date")
    private LocalDateTime date;

    @Field("reason")
    private String reason;
}
