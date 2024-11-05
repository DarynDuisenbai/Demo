package com.example.demo.mappers.impl;

import com.example.demo.dtos.requests.CreateConclusionRequest;
import com.example.demo.dtos.responces.AgreementDto;
import com.example.demo.dtos.responces.ConclusionDto;
import com.example.demo.mappers.ConclusionMapper;
import com.example.demo.models.Conclusion;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ConclusionMapperImpl implements ConclusionMapper {
    @Override
    public Conclusion fromCreateToConclusion(CreateConclusionRequest createConclusionRequest) {
        Conclusion conclusion = new Conclusion();
        conclusion.setUD(createConclusionRequest.getUD());
        conclusion.setIINofCalled(createConclusionRequest.getIIN());
        conclusion.setBINorIINOfCalled(createConclusionRequest.getBIN_IIN());
        conclusion.setJobTitleOfCalled(createConclusionRequest.getJobTitle());
        conclusion.setRegion(createConclusionRequest.getRegion());
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
        dto.setCreationDate(conclusion.getCreationDate().toString());
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
        dto.setStatus(conclusion.getStatus());
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

}
