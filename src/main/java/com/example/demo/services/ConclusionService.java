package com.example.demo.services;

import com.example.demo.dtos.requests.*;
import com.example.demo.dtos.responces.AgreementDto;
import com.example.demo.dtos.responces.ConclusionDto;
import com.example.demo.dtos.responces.History;
import com.example.demo.dtos.responces.TempConclusionDto;
import com.example.demo.exceptions.*;
import com.example.demo.models.Agreement;
import com.example.demo.models.Conclusion;

import java.util.List;
import java.util.Set;

public interface ConclusionService {
    void createConclusion(CreateConclusionRequest createConclusionRequest) throws UserNotFoundException, RegionNotFoundException, CaseNotFound;
    void saveConclusion(CreateConclusionRequest createConclusionRequest) throws RegionNotFoundException, UserNotFoundException, CaseNotFound;
    void editSavedConclusion(EditSavedConclusionRequest editSavedConclusionRequest) throws UserNotFoundException, NoTemporaryConclusionFound, RegionNotFoundException, CaseNotFound;
    void turnToPermanent(String registrationNumber) throws UserNotFoundException, NoTemporaryConclusionFound;
    Set<ConclusionDto> filter(FilterRequest filterRequest) throws UserNotFoundException;
    Set<ConclusionDto> userConclusions(String IIN) throws UserNotFoundException;
    List<TempConclusionDto> userSavedConclusions(String IIN) throws UserNotFoundException;
    List<AgreementDto> userAgreements(String IIN) throws UserNotFoundException;
    List<String> allUD();
    List<Conclusion> getAllConclusions();

    void sendConclusion(Conclusion conclusion, String IIN) throws UserNotFoundException;
    void sendAgreement(Agreement agreement, String IIN) throws UserNotFoundException;
    AgreementDto makeDecision(DecisionRequest decisionRequest) throws UserNotFoundException, NoConclusionException;
    ConclusionDto getSpecific(String regNumber) throws NoConclusionException;
    History history(String iinInvestigator, String goal) throws UserNotFoundException;

}
