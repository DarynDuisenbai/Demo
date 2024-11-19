package com.example.demo.dtos.responces;

import com.example.demo.models.Agreement;
import com.example.demo.models.Conclusion;
import com.example.demo.models.Status;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class History {
    private List<Agreement> agreements;
    private String goal;
}
