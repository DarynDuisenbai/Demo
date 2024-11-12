package com.example.demo.services.impl;

import com.example.demo.exceptions.CaseNotFound;
import com.example.demo.models.Case;
import com.example.demo.repository.CaseRepository;
import com.example.demo.services.CaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CaseServiceImpl implements CaseService {
    private final CaseRepository caseRepository;

    @Override
    public Case getCaseByUD(String UD) throws CaseNotFound {
        return caseRepository.findCaseByUD(UD).orElseThrow(() -> new CaseNotFound("Case not found."));
    }
}
