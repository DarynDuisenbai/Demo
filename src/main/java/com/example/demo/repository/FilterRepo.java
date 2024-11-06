package com.example.demo.repository;

import com.example.demo.dtos.requests.FilterRequest;
import com.example.demo.dtos.responces.ConclusionDto;
import com.example.demo.models.Conclusion;

import java.time.LocalDate;
import java.util.List;

public interface FilterRepo {
    List<Conclusion> filterConclusions(FilterRequest filterRequest);
}
