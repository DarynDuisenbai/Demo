package com.example.demo.service.spec;

import com.example.demo.domain.User;
import com.example.demo.dto.request.conclusion.CreateConclusionRequest;
import com.example.demo.dto.request.conclusion.EditSavedConclusionRequest;
import com.example.demo.dto.request.conclusion.FilterRequest;
import com.example.demo.dto.request.user.DecisionRequest;
import com.example.demo.dto.responce.AgreementDto;
import com.example.demo.dto.responce.ConclusionDto;
import com.example.demo.dto.responce.History;
import com.example.demo.dto.responce.TempConclusionDto;
import com.example.demo.exception.*;
import com.example.demo.domain.Agreement;
import com.example.demo.domain.Conclusion;

import java.util.List;
import java.util.Set;

public interface ConclusionService {
    void createConclusion(CreateConclusionRequest createConclusionRequest) throws UserNotFoundException, RegionNotFoundException, CaseNotFound;
    void saveConclusion(CreateConclusionRequest createConclusionRequest) throws RegionNotFoundException, UserNotFoundException, CaseNotFound;
    void editSavedConclusion(EditSavedConclusionRequest editSavedConclusionRequest) throws UserNotFoundException,
            NoTemporaryConclusionFound, RegionNotFoundException, CaseNotFound;
    void deleteConclusion(String registrationNumber) throws UserNotFoundException, ConclusionNotReadyException, NoConclusionException;
    void turnToPermanent(String registrationNumber) throws UserNotFoundException, NoTemporaryConclusionFound, ConclusionNotReadyException;
    void remakeConclusion(EditSavedConclusionRequest editSavedConclusionRequest) throws NoConclusionException, NoPermissionForUpdateException, CaseNotFound, RegionNotFoundException;

    Set<ConclusionDto> filter(FilterRequest filterRequest) throws UserNotFoundException;
    List<ConclusionDto> userConclusions(String IIN) throws UserNotFoundException;
    List<TempConclusionDto> userSavedConclusions(String IIN) throws UserNotFoundException;
    List<AgreementDto> userAgreements(String IIN) throws UserNotFoundException;
    List<String> allUD();
    List<Conclusion> getAllConclusions();

    void sendConclusion(Conclusion conclusion, User manager) throws UserNotFoundException;
    AgreementDto makeDecision(DecisionRequest decisionRequest) throws UserNotFoundException, NoConclusionException;
    ConclusionDto getSpecific(String regNumber, String iin) throws NoConclusionException, UserNotFoundException, AccessDeniedException;
    TempConclusionDto getSpecificTemp(String regNumber, String iin) throws NoConclusionException, UserNotFoundException, AccessDeniedException;
    History history(String iinUser, String iinOfCalled, String goal) throws UserNotFoundException, NoConclusionException;


}
