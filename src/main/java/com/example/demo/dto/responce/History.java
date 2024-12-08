package com.example.demo.dto.responce;

import com.example.demo.domain.Agreement;
import lombok.Getter;
import lombok.Setter;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class History {
    private String prevCall;
    private String fullName;
    private String status;
    private LocalDateTime came;
    private LocalDateTime leave;
    private String goal;
}
