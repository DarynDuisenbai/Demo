package com.example.demo.models;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

@Getter
@Setter
@Data
@Document(collection = "users")
public class User {
    @Id
    private String _id;

    @Field("name")
    private String name;

    @Field("password")
    private String password;

    @Field("email")
    private String email;

    @Field("registrationDate")
    private Date registrationDate;

    @Field("profileImage")
    private String profileImage;
}
