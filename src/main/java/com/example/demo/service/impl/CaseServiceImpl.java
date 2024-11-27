package com.example.demo.service.impl;

import com.example.demo.exception.CaseNotFound;
import com.example.demo.domain.Case;
import com.example.demo.repository.spec.CaseRepository;
import com.example.demo.service.spec.CaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CaseServiceImpl implements CaseService {
    private final CaseRepository caseRepository;

    @Override
    public Case getCaseByUD(String UD) throws CaseNotFound {
        return caseRepository.findCaseByUD(UD).orElseThrow(() -> new CaseNotFound("Case with UD: " +UD+" not found."));
    }

    @Override
    public List<Case> allCases() {
        return caseRepository.findAll();
    }
}
