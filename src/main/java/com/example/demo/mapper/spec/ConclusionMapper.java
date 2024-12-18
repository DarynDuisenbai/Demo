package com.example.demo.mapper.spec;

import com.example.demo.dto.request.conclusion.CreateConclusionRequest;
import com.example.demo.dto.responce.ConclusionDto;
import com.example.demo.exception.RegionNotFoundException;
import com.example.demo.domain.Conclusion;
import com.example.demo.domain.TemporaryConclusion;
import com.example.demo.exception.UserNotFoundException;

import java.util.List;
import java.util.Set;

public interface ConclusionMapper {
    Conclusion fromCreateToConclusion(CreateConclusionRequest createConclusionRequest) throws RegionNotFoundException;
    ConclusionDto  toConclusionDto(Conclusion conclusion) throws UserNotFoundException;
    List<ConclusionDto> toDtoList(List<Conclusion> conclusions) throws UserNotFoundException;
    Set<ConclusionDto> toDtoSet(List<Conclusion> conclusions);
    Conclusion fromTempToConclusion(TemporaryConclusion tempConclusion);
    List<Conclusion> fromTempListToConclusionList(List<TemporaryConclusion> tempConclusionDtos);



}
