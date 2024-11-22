package com.example.demo.domain;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@Setter
@Data
@Document(collection = "regions")
public class Region {
    @Id
    private String _id;

    @Field("name")
    private String name;


}
