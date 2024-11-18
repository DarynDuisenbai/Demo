package com.example.demo.mappers;

import com.example.demo.dtos.requests.CreateConclusionRequest;
import com.example.demo.dtos.responces.TempConclusionDto;
import com.example.demo.exceptions.RegionNotFoundException;
import com.example.demo.exceptions.UserNotFoundException;
import com.example.demo.models.TemporaryConclusion;

import java.util.List;

public interface TempMapper {
    TemporaryConclusion fromCreateToTemp(CreateConclusionRequest createConclusionRequest) throws RegionNotFoundException, UserNotFoundException;
    TempConclusionDto toTempConclusionDto(TemporaryConclusion temporaryConclusion);
    List<TempConclusionDto> toDtoList(List<TemporaryConclusion> temporaryConclusions);
    TemporaryConclusion formDtoToTempConclusion(TempConclusionDto tempConclusionDto) throws UserNotFoundException;

}
