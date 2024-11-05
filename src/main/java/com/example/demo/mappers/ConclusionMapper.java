package com.example.demo.mappers;

import com.example.demo.dtos.requests.CreateConclusionRequest;
import com.example.demo.dtos.responces.AgreementDto;
import com.example.demo.dtos.responces.ConclusionDto;
import com.example.demo.models.Conclusion;

import java.util.List;

public interface ConclusionMapper {
    Conclusion fromCreateToConclusion(CreateConclusionRequest createConclusionRequest);
    ConclusionDto  toConclusionDto(Conclusion conclusion);
    List<ConclusionDto> toDtoList(List<Conclusion> conclusions);

}
