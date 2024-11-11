package com.example.demo.repository;

import com.example.demo.dtos.requests.FilterRequest;
import com.example.demo.models.Conclusion;

import java.util.List;

public interface FilterConclusionRepo {
    List<Conclusion> filterConclusions(FilterRequest filterRequest);
}