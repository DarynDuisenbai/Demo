package com.example.demo.domain;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Data
@Document(collection = "users")
public class User {
    public User() {
        this.conclusionsRegNumbers = new ArrayList<>();
        this.temporaryConclusionsRegNumbers = new ArrayList<>();
        this.receivedConclusionsRegNumbers = new ArrayList<>();
        this.agreements = new ArrayList<>();
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
    private List<String> conclusionsRegNumbers;

    @Field("tempDocs")
    private List<String> temporaryConclusionsRegNumbers;

    @Field("receivedDocs")
    private List<String> receivedConclusionsRegNumbers;

    @Field("agreement")
    private List<Agreement> agreements;

    @Field("manager")
    private String managerIIN;

    @Field("role")
    private String role;

}
