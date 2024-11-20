package com.example.demo.repository;

import com.example.demo.dtos.requests.FilterRequest;
import com.example.demo.models.Conclusion;

import java.util.List;
import java.util.Set;

public interface FilterConclusionRepo {
    List<Conclusion> filterAllConclusions(FilterRequest filterRequest);
    List<Conclusion> filterSomeConclusions(List<Conclusion> conclusions, FilterRequest filterRequest);
}
