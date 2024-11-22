package com.example.demo.service.spec;

import com.example.demo.exception.CaseNotFound;
import com.example.demo.domain.Case;

import java.util.List;

public interface CaseService {
    Case getCaseByUD(String UD) throws CaseNotFound;
    List<Case> allCases();
}
