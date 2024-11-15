package com.example.demo.services;

import com.example.demo.exceptions.CaseNotFound;
import com.example.demo.models.Case;

import java.util.List;

public interface CaseService {
    Case getCaseByUD(String UD) throws CaseNotFound;
    List<Case> allCases();
}
