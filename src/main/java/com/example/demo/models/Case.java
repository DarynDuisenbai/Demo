package com.example.demo.models;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Data
@Document(collection = "cases")
public class Case {
    @Id
    private String _id;

    @Field("registrationDate")
    private LocalDateTime registrationDate;

    @Field("UD")
    private String UD;

    @Field("article")
    private String article;

    @Field("decision")
    private String decision;

    @Field("summary")
    private String summary;
}
