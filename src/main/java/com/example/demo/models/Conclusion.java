package com.example.demo.models;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@Setter
@Data
@Document(collection = "conclusions")
public class Conclusion {
    @Id
    private String _id;

    @Field("registrationNumber")
    private String registrationNumber;



}
