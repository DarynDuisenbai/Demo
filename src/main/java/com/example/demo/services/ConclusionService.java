package com.example.demo.services;

import com.example.demo.dtos.requests.CreateConclusionRequest;
import com.example.demo.dtos.requests.FilterRequest;
import com.example.demo.dtos.requests.ShortConclusionRequest;
import com.example.demo.dtos.requests.UserConclusionRequest;
import com.example.demo.dtos.responces.AgreementDto;
import com.example.demo.dtos.responces.ConclusionDto;
import com.example.demo.exceptions.UserNotFoundException;

import java.util.List;

public interface ConclusionService {
    void createConclusion(CreateConclusionRequest createConclusionRequest) throws UserNotFoundException;
    List<ConclusionDto> filter(FilterRequest filterRequest) throws UserNotFoundException;
    //List<AgreementDto> agreement() throws UserNotFoundException;
    List<ConclusionDto> userConclusions(UserConclusionRequest userConclusionRequest) throws UserNotFoundException;

}
