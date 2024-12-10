package com.example.demo.mapper.impl;

import com.example.demo.dto.request.conclusion.CreateConclusionRequest;
import com.example.demo.dto.responce.TempConclusionDto;
import com.example.demo.exception.RegionNotFoundException;
import com.example.demo.exception.UserNotFoundException;
import com.example.demo.mapper.spec.TempMapper;
import com.example.demo.domain.Region;
import com.example.demo.domain.Status;
import com.example.demo.domain.TemporaryConclusion;
import com.example.demo.domain.User;
import com.example.demo.repository.spec.RegionRepository;
import com.example.demo.repository.spec.StatusRepository;
import com.example.demo.repository.spec.UserRepository;
import com.example.demo.util.Generator;
import com.example.demo.util.UTCFormatter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class TempMapperImpl implements TempMapper {
    private final RegionRepository regionRepository;
    private final UTCFormatter utcFormatter;
    private final Generator generator;
    @Override
    public TemporaryConclusion fromCreateToTemp(CreateConclusionRequest createConclusionRequest) throws RegionNotFoundException, UserNotFoundException {
        TemporaryConclusion tempConclusion = new TemporaryConclusion();
        tempConclusion.setBINorIINOfCalled(createConclusionRequest.getBIN_IIN());
        tempConclusion.setJobTitleOfCalled(createConclusionRequest.getJobTitle());

        Region region = regionRepository.findRegionByName(createConclusionRequest.getRegion()).
                orElseThrow(() -> new RegionNotFoundException("Region not found."));

        tempConclusion.setRegion(region);
        tempConclusion.setPlannedActions(createConclusionRequest.getPlannedActions());
        tempConclusion.setEventTime(utcFormatter.convertUTCToUTCPlus5(createConclusionRequest.getEventDateTime()));
        tempConclusion.setEventPlace(createConclusionRequest.getEventPlace());
        tempConclusion.setRelation(createConclusionRequest.getRelation());
        tempConclusion.setInvestigation(createConclusionRequest.getInvestigationType());
        tempConclusion.setIsBusiness(createConclusionRequest.getRelatesToBusiness());
        tempConclusion.setWorkPlaceBusiness(generator.generateWorkPlaceBusiness());
        tempConclusion.setBINOrIINofBusiness(generator.generateBIN());
        tempConclusion.setIINDefender(createConclusionRequest.getIINDefender());
        tempConclusion.setIINofCalled(createConclusionRequest.getIINOfCalled());
        tempConclusion.setJustification(createConclusionRequest.getJustification());
        tempConclusion.setResult(createConclusionRequest.getResult());
        tempConclusion.setJobPlace(generator.generateJobPlaces());

        String calledName = generator.generateNames();
        String defenderName = generator.generateNames();

        while (defenderName.equals(calledName)) {
            defenderName = generator.generateNames();
        }

        tempConclusion.setFullNameOfCalled(calledName);
        tempConclusion.setFullNameOfDefender(defenderName);
        return tempConclusion;
    }

    @Override
    public TempConclusionDto toTempConclusionDto(TemporaryConclusion temporaryConclusion){
        TempConclusionDto dto = new TempConclusionDto();
        dto.setRegistrationNumber(temporaryConclusion.getRegistrationNumber());
        dto.setCreationDate(utcFormatter.convertUTCToUTCPlus5(temporaryConclusion.getCreationDate()));
        dto.setUdNumber(temporaryConclusion.getUD());
        dto.setRegistrationDate(utcFormatter.convertUTCToUTCPlus5(temporaryConclusion.getRegistrationDate()));
        dto.setArticle(temporaryConclusion.getArticle());
        dto.setDecision(temporaryConclusion.getDecision());
        dto.setSummary(temporaryConclusion.getDecision());
        dto.setCalledPersonIIN(temporaryConclusion.getIINofCalled());
        dto.setCalledPersonFullName(temporaryConclusion.getFullNameOfCalled());
        dto.setCalledPersonPosition(temporaryConclusion.getJobTitleOfCalled());
        dto.setCalledPersonBIN(temporaryConclusion.getBINorIINOfCalled());
        dto.setWorkPlace(temporaryConclusion.getJobPlace());
        dto.setRegion(temporaryConclusion.getRegion());
        dto.setPlannedInvestigativeActions(temporaryConclusion.getPlannedActions());
        dto.setEventDateTime(utcFormatter.convertUTCToUTCPlus5(temporaryConclusion.getEventTime()));
        dto.setEventPlace(temporaryConclusion.getEventPlace());
        dto.setInvestigatorIIN(temporaryConclusion.getInvestigatorIIN());
        dto.setStatus(temporaryConclusion.getStatus().getName());
        dto.setRelationToEvent(temporaryConclusion.getRelation());
        dto.setInvestigationTypes(temporaryConclusion.getInvestigation());
        dto.setRelatesToBusiness(temporaryConclusion.getIsBusiness());
        dto.setWorkPlaceBusiness(temporaryConclusion.getWorkPlaceBusiness());
        dto.setBINOrIINofBusiness(temporaryConclusion.getBINOrIINofBusiness());
        dto.setDefenseAttorneyIIN(temporaryConclusion.getIINDefender());
        dto.setDefenseAttorneyFullName(temporaryConclusion.getFullNameOfDefender());
        dto.setJustification(temporaryConclusion.getJustification());
        dto.setResult(temporaryConclusion.getResult());

        return dto;
    }

    @Override
    public List<TempConclusionDto> toDtoList(List<TemporaryConclusion> temporaryConclusions) throws UserNotFoundException {
        List<TempConclusionDto> dtos = new ArrayList<>();
        for(TemporaryConclusion conclusion : temporaryConclusions){
            dtos.add(toTempConclusionDto(conclusion));
        }

        return dtos;
    }
}
