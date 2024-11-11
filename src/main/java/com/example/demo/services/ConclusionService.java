package com.example.demo.services;

import com.example.demo.dtos.requests.*;
import com.example.demo.dtos.responces.AgreementDto;
import com.example.demo.dtos.responces.ConclusionDto;
import com.example.demo.dtos.responces.TempConclusionDto;
import com.example.demo.exceptions.NoTemporaryConclusionFound;
import com.example.demo.exceptions.RegionNotFoundException;
import com.example.demo.exceptions.UserNotFoundException;

import java.util.List;

public interface ConclusionService {
    void createConclusion(CreateConclusionRequest createConclusionRequest) throws UserNotFoundException, RegionNotFoundException;
    void saveConclusion(CreateConclusionRequest createConclusionRequest) throws RegionNotFoundException, UserNotFoundException;
    void editSavedConclusion(EditSavedConclusionRequest editSavedConclusionRequest) throws UserNotFoundException, NoTemporaryConclusionFound, RegionNotFoundException;
    void turnToPermanent(String registrationNumber) throws UserNotFoundException, NoTemporaryConclusionFound;
    List<ConclusionDto> filter(FilterRequest filterRequest);
    //List<AgreementDto> agreement() throws UserNotFoundException;
    List<ConclusionDto> userConclusions(String IIN) throws UserNotFoundException;
    List<TempConclusionDto> userSavedConclusions(String IIN) throws UserNotFoundException;
    List<String> allUD();
    List<ConclusionDto> getAllConclusions();
}
