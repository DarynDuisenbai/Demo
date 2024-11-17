package com.example.demo.mappers.impl;

import com.example.demo.dtos.requests.CreateConclusionRequest;
import com.example.demo.dtos.responces.AgreementDto;
import com.example.demo.dtos.responces.ConclusionDto;
import com.example.demo.dtos.responces.TempConclusionDto;
import com.example.demo.exceptions.RegionNotFoundException;
import com.example.demo.mappers.ConclusionMapper;
import com.example.demo.mappers.TempMapper;
import com.example.demo.models.Conclusion;
import com.example.demo.models.Region;
import com.example.demo.models.Status;
import com.example.demo.models.TemporaryConclusion;
import com.example.demo.repository.RegionRepository;
import com.example.demo.repository.StatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ConclusionMapperImpl implements ConclusionMapper {
    private final RegionRepository regionRepository;
    private final TempMapper tempMapper;

    @Override
    public Conclusion fromCreateToConclusion(CreateConclusionRequest createConclusionRequest) throws RegionNotFoundException {
        Conclusion conclusion = new Conclusion();
        conclusion.setBINorIINOfCalled(createConclusionRequest.getBIN_IIN());
        conclusion.setJobTitleOfCalled(createConclusionRequest.getJobTitle());

        Region region = regionRepository.findRegionByName(createConclusionRequest.getRegion()).
                orElseThrow(() -> new RegionNotFoundException("Region not found."));

        conclusion.setRegion(region);
        conclusion.setPlannedActions(createConclusionRequest.getPlannedActions());
        conclusion.setEventTime(createConclusionRequest.getEventDateTime());
        conclusion.setEventPlace(createConclusionRequest.getEventPlace());
        conclusion.setRelation(createConclusionRequest.getRelation());
        conclusion.setInvestigation(createConclusionRequest.getInvestigationType());
        conclusion.setBusiness(createConclusionRequest.getRelatesToBusiness() != null ? createConclusionRequest.getRelatesToBusiness() : false);
        conclusion.setIINDefender(createConclusionRequest.getIINDefender());
        conclusion.setJustification(createConclusionRequest.getJustification());
        conclusion.setResult(createConclusionRequest.getResult());

        return conclusion;
    }

    @Override
    public ConclusionDto toConclusionDto(Conclusion conclusion) {
        ConclusionDto dto = new ConclusionDto();
        dto.setRegistrationNumber(conclusion.getRegistrationNumber());
        dto.setCreationDate(conclusion.getCreationDate());
        dto.setUdNumber(conclusion.getUD());
        dto.setRegistrationDate(conclusion.getRegistrationDate());
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
        dto.setEventDateTime(conclusion.getEventTime());
        dto.setEventPlace(conclusion.getEventPlace());
        dto.setInvestigator(conclusion.getInvestigator());
        dto.setStatus(conclusion.getStatus().getName());
        dto.setRelationToEvent(conclusion.getRelation());
        dto.setInvestigationTypes(conclusion.getInvestigation());
        dto.setRelatesToBusiness(conclusion.isBusiness());
        dto.setDefenseAttorneyIIN(conclusion.getIINDefender());
        dto.setDefenseAttorneyFullName(conclusion.getFullNameOfDefender());
        dto.setJustification(conclusion.getJustification());
        dto.setResult(conclusion.getResult());

        return dto;
    }

    @Override
    public List<ConclusionDto> toDtoList(List<Conclusion> conclusions) {
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
        conclusion.setCreationDate(tempConclusionDto.getCreationDate());
        conclusion.setUD(tempConclusionDto.getUD());
        conclusion.setRegistrationDate(tempConclusionDto.getRegistrationDate());
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
        conclusion.setEventTime(tempConclusionDto.getEventTime());
        conclusion.setEventPlace(tempConclusionDto.getEventPlace());
        conclusion.setInvestigator(tempConclusionDto.getInvestigator());
        conclusion.setStatus(tempConclusionDto.getStatus());
        conclusion.setRelation(tempConclusionDto.getRelation());
        conclusion.setInvestigation(tempConclusionDto.getInvestigation());
        conclusion.setBusiness(tempConclusionDto.isBusiness());
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
}
