package com.example.demo.mappers;

import com.example.demo.dtos.requests.CreateConclusionRequest;
import com.example.demo.dtos.responces.AgreementDto;
import com.example.demo.dtos.responces.ConclusionDto;
import com.example.demo.dtos.responces.TempConclusionDto;
import com.example.demo.exceptions.RegionNotFoundException;
import com.example.demo.models.Conclusion;
import com.example.demo.models.TemporaryConclusion;

import java.util.List;

public interface ConclusionMapper {
    Conclusion fromCreateToConclusion(CreateConclusionRequest createConclusionRequest) throws RegionNotFoundException;
    ConclusionDto  toConclusionDto(Conclusion conclusion);
    List<ConclusionDto> toDtoList(List<Conclusion> conclusions);
    Conclusion fromTempToConclusion(TemporaryConclusion tempConclusion);


}
