package com.example.demo.repository.spec;

import com.example.demo.dto.request.conclusion.FilterRequest;
import com.example.demo.domain.Conclusion;

import java.util.List;

public interface FilterConclusionRepo {
    List<Conclusion> filterAllConclusions(FilterRequest filterRequest);
    List<Conclusion> filterSomeConclusions(List<Conclusion> conclusions, FilterRequest filterRequest);
}
