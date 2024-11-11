package com.example.demo.models;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Data
@Document(collection = "users")
public class User {
    public User() {
        this.conclusions = new ArrayList<>();
        this.temporaryConclusions = new ArrayList<>();
    }

    @Id
    private String _id;

    @Field("name")
    private String name;

    @Field("secondName")
    private String secondName;

    @Field("password")
    private String password;

    @Field("email")
    @Indexed(unique = true)
    private String email;

    @Field("registrationDate")
    private LocalDateTime registrationDate;

    @Field("profileImage")
    private String profileImage;

    @Field("IIN")
    private String IIN;

    @Field("jobTitle")
    private JobTitle job;

    @Field("department")
    private Department department;

    @Field("docs")
    private List<Conclusion> conclusions;

    @Field("tempDocs")
    private List<TemporaryConclusion> temporaryConclusions;

    @Field("role")
    private String role;

}
