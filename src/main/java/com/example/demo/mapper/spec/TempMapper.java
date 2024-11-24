package com.example.demo.mapper.spec;

import com.example.demo.dto.request.conclusion.CreateConclusionRequest;
import com.example.demo.dto.responce.TempConclusionDto;
import com.example.demo.exception.RegionNotFoundException;
import com.example.demo.exception.UserNotFoundException;
import com.example.demo.domain.TemporaryConclusion;

import java.util.List;

public interface TempMapper {
    TemporaryConclusion fromCreateToTemp(CreateConclusionRequest createConclusionRequest) throws RegionNotFoundException, UserNotFoundException;
    TempConclusionDto toTempConclusionDto(TemporaryConclusion temporaryConclusion) throws UserNotFoundException;
    List<TempConclusionDto> toDtoList(List<TemporaryConclusion> temporaryConclusions) throws UserNotFoundException;
}
