package com.example.demo.mapper.impl;

import com.example.demo.domain.Status;
import com.example.demo.dto.request.conclusion.CreateConclusionRequest;
import com.example.demo.dto.responce.ConclusionDto;
import com.example.demo.exception.RegionNotFoundException;
import com.example.demo.exception.UserNotFoundException;
import com.example.demo.mapper.spec.ConclusionMapper;
import com.example.demo.domain.Conclusion;
import com.example.demo.domain.Region;
import com.example.demo.domain.TemporaryConclusion;
import com.example.demo.repository.spec.RegionRepository;
import com.example.demo.repository.spec.StatusRepository;
import com.example.demo.util.UTCFormatter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class ConclusionMapperImpl implements ConclusionMapper {
    private final RegionRepository regionRepository;
    private final StatusRepository statusRepository;
    private final UTCFormatter utcFormatter;

    @Override
    public Conclusion fromCreateToConclusion(CreateConclusionRequest createConclusionRequest) throws RegionNotFoundException {
        Conclusion conclusion = new Conclusion();
        conclusion.setBINorIINOfCalled(createConclusionRequest.getBIN_IIN());
        conclusion.setJobTitleOfCalled(createConclusionRequest.getJobTitle());

        Region region = regionRepository.findRegionByName(createConclusionRequest.getRegion()).
                orElseThrow(() -> new RegionNotFoundException("Region not found."));

        conclusion.setRegion(region);
        conclusion.setPlannedActions(createConclusionRequest.getPlannedActions());
        conclusion.setEventTime(utcFormatter.convertUTCToUTCPlus5(createConclusionRequest.getEventDateTime()));
        conclusion.setEventPlace(createConclusionRequest.getEventPlace());
        conclusion.setRelation(createConclusionRequest.getRelation());
        conclusion.setInvestigation(createConclusionRequest.getInvestigationType());
        conclusion.setBusiness(createConclusionRequest.getRelatesToBusiness());
        conclusion.setIINDefender(createConclusionRequest.getIINDefender());
        conclusion.setIINofCalled(createConclusionRequest.getIINOfCalled());
        conclusion.setJustification(createConclusionRequest.getJustification());
        conclusion.setResult(createConclusionRequest.getResult());

        return conclusion;
    }

    @Override
    public ConclusionDto toConclusionDto(Conclusion conclusion){
        ConclusionDto dto = new ConclusionDto();
        dto.setRegistrationNumber(conclusion.getRegistrationNumber());
        dto.setCreationDate(utcFormatter.convertUTCToUTCPlus5(conclusion.getCreationDate()));
        dto.setUdNumber(conclusion.getUD());
        dto.setRegistrationDate(utcFormatter.convertUTCToUTCPlus5(conclusion.getRegistrationDate()));
        dto.setArticle(conclusion.getArticle());
        dto.setDecision(conclusion.getDecision());
        dto.setSummary(conclusion.getDecision());
        dto.setCalledPersonIIN(conclusion.getIINofCalled());
        dto.setCalledPersonFullName(conclusion.getFullNameOfCalled());
        dto.setCalledPersonPosition(conclusion.getJobTitleOfCalled());
        dto.setCalledPersonBIN(conclusion.getBINorIINOfCalled());
        dto.setWorkPlace(conclusion.getEventPlace());
        dto.setRegion(conclusion.getRegion());
        dto.setPlannedInvestigativeActions(conclusion.getPlannedActions());
        dto.setEventDateTime(utcFormatter.convertUTCToUTCPlus5(conclusion.getEventTime()));
        dto.setEventPlace(conclusion.getEventPlace());
        dto.setInvestigatorIIN(conclusion.getInvestigatorIIN());
        dto.setStatus(conclusion.getStatus().getName());
        dto.setRelationToEvent(conclusion.getRelation());
        dto.setInvestigationTypes(conclusion.getInvestigation());
        dto.setRelatesToBusiness(conclusion.getBusiness());
        dto.setDefenseAttorneyIIN(conclusion.getIINDefender());
        dto.setDefenseAttorneyFullName(conclusion.getFullNameOfDefender());
        dto.setJustification(conclusion.getJustification());
        dto.setResult(conclusion.getResult());

        return dto;
    }

    @Override
    public List<ConclusionDto> toDtoList(List<Conclusion> conclusions) throws UserNotFoundException {
        List<ConclusionDto> dtos = new ArrayList<>();
        for(Conclusion conclusion : conclusions){
            dtos.add(toConclusionDto(conclusion));
        }

        return dtos;
    }
    @Override
    public Conclusion fromTempToConclusion(TemporaryConclusion tempConclusionDto) {
        Conclusion conclusion = new Conclusion();
        conclusion.setRegistrationNumber(tempConclusionDto.getRegistrationNumber());
        conclusion.setCreationDate(utcFormatter.convertUTCToUTCPlus5(tempConclusionDto.getCreationDate()));
        conclusion.setUD(tempConclusionDto.getUD());
        conclusion.setRegistrationDate(utcFormatter.convertUTCToUTCPlus5(tempConclusionDto.getRegistrationDate()));
        conclusion.setArticle(tempConclusionDto.getArticle());
        conclusion.setDecision(tempConclusionDto.getDecision());
        conclusion.setPlot(tempConclusionDto.getDecision());
        conclusion.setIINofCalled(tempConclusionDto.getIINofCalled());
        conclusion.setFullNameOfCalled(tempConclusionDto.getFullNameOfCalled());
        conclusion.setJobTitleOfCalled(tempConclusionDto.getJobTitleOfCalled());
        conclusion.setBINorIINOfCalled(tempConclusionDto.getBINorIINOfCalled());
        conclusion.setEventPlace(tempConclusionDto.getEventPlace());
        conclusion.setRegion(tempConclusionDto.getRegion());
        conclusion.setPlannedActions(tempConclusionDto.getPlannedActions());
        conclusion.setEventTime(utcFormatter.convertUTCToUTCPlus5(tempConclusionDto.getEventTime()));
        conclusion.setEventPlace(tempConclusionDto.getEventPlace());
        conclusion.setInvestigatorIIN(tempConclusionDto.getInvestigatorIIN());
        conclusion.setRelation(tempConclusionDto.getRelation());
        conclusion.setInvestigation(tempConclusionDto.getInvestigation());
        conclusion.setBusiness(tempConclusionDto.getIsBusiness());
        conclusion.setIINDefender(tempConclusionDto.getIINDefender());
        conclusion.setFullNameOfDefender(tempConclusionDto.getFullNameOfDefender());
        conclusion.setJustification(tempConclusionDto.getJustification());
        conclusion.setResult(tempConclusionDto.getResult());

        return conclusion;
    }

    @Override
    public List<Conclusion> fromTempListToConclusionList(List<TemporaryConclusion> tempConclusions) {
        List<Conclusion> conclusions = new ArrayList<>();
        for(TemporaryConclusion temporaryConclusion : tempConclusions){
            conclusions.add(fromTempToConclusion(temporaryConclusion));
        }

        return conclusions;
    }

    @Override
    public Set<ConclusionDto> toDtoSet(List<Conclusion> conclusions) {
        Set<ConclusionDto> conclusionDtos = new HashSet<>();
        for(Conclusion conclusion : conclusions){
            conclusionDtos.add(toConclusionDto(conclusion));
        }
        return conclusionDtos;
    }

    @Override
    public Set<ConclusionDto> toDtoSet(Set<Conclusion> conclusions) {
        Set<ConclusionDto> conclusionDtos = new HashSet<>();
        for(Conclusion conclusion : conclusions){
            conclusionDtos.add(toConclusionDto(conclusion));
        }
        return conclusionDtos;
    }
}
