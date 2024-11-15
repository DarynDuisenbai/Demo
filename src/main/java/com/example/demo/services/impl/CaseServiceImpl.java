package com.example.demo.services.impl;

import com.example.demo.exceptions.CaseNotFound;
import com.example.demo.models.Case;
import com.example.demo.repository.CaseRepository;
import com.example.demo.services.CaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CaseServiceImpl implements CaseService {
    private final CaseRepository caseRepository;

    @Override
    public Case getCaseByUD(String UD) throws CaseNotFound {
        return caseRepository.findCaseByUD(UD).orElseThrow(() -> new CaseNotFound("Case not found."));
    }

    @Override
    public List<Case> allCases() {
        return caseRepository.findAll();
    }
}
