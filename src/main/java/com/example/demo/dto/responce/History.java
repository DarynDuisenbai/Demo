package com.example.demo.dto.responce;

import com.example.demo.domain.Agreement;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class History {
    private List<Agreement> agreements;
    private String goal;
}
