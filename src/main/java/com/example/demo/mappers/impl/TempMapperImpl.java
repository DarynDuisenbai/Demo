package com.example.demo.mappers.impl;

import com.example.demo.dtos.requests.CreateConclusionRequest;
import com.example.demo.dtos.responces.TempConclusionDto;
import com.example.demo.exceptions.RegionNotFoundException;
import com.example.demo.exceptions.UserNotFoundException;
import com.example.demo.mappers.TempMapper;
import com.example.demo.models.Region;
import com.example.demo.models.Status;
import com.example.demo.models.TemporaryConclusion;
import com.example.demo.models.User;
import com.example.demo.repository.RegionRepository;
import com.example.demo.repository.StatusRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.utils.UTCFormatter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class TempMapperImpl implements TempMapper {
    private final RegionRepository regionRepository;
    private final StatusRepository statusRepository;
    private final UserRepository userRepository;
    private final UTCFormatter utcFormatter;
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
        tempConclusion.setBusiness(createConclusionRequest.getRelatesToBusiness() != null ? createConclusionRequest.getRelatesToBusiness() : false);
        tempConclusion.setIINDefender(createConclusionRequest.getIINDefender());
        tempConclusion.setIINofCalled(createConclusionRequest.getIINOfCalled());
        tempConclusion.setJustification(createConclusionRequest.getJustification());
        tempConclusion.setResult(createConclusionRequest.getResult());

        return tempConclusion;
    }

    @Override
    public TempConclusionDto toTempConclusionDto(TemporaryConclusion temporaryConclusion) {
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
        dto.setWorkPlace(temporaryConclusion.getEventPlace());
        dto.setRegion(temporaryConclusion.getRegion());
        dto.setPlannedInvestigativeActions(temporaryConclusion.getPlannedActions());
        dto.setEventDateTime(utcFormatter.convertUTCToUTCPlus5(temporaryConclusion.getEventTime()));
        dto.setEventPlace(temporaryConclusion.getEventPlace());
        dto.setInvestigator(temporaryConclusion.getInvestigator().getName() + " " + temporaryConclusion.getInvestigator().getSecondName());
        dto.setStatus(temporaryConclusion.getStatus().getName());
        dto.setRelationToEvent(temporaryConclusion.getRelation());
        dto.setInvestigationTypes(temporaryConclusion.getInvestigation());
        dto.setRelatesToBusiness(temporaryConclusion.isBusiness());
        dto.setDefenseAttorneyIIN(temporaryConclusion.getIINDefender());
        dto.setDefenseAttorneyFullName(temporaryConclusion.getFullNameOfDefender());
        dto.setJustification(temporaryConclusion.getJustification());
        dto.setResult(temporaryConclusion.getResult());

        return dto;
    }

    @Override
    public List<TempConclusionDto> toDtoList(List<TemporaryConclusion> temporaryConclusions) {
        List<TempConclusionDto> dtos = new ArrayList<>();
        for(TemporaryConclusion conclusion : temporaryConclusions){
            dtos.add(toTempConclusionDto(conclusion));
        }

        return dtos;
    }

    @Override
    public TemporaryConclusion formDtoToTempConclusion(TempConclusionDto tempConclusionDto) throws UserNotFoundException {
        TemporaryConclusion temporaryConclusion = new TemporaryConclusion();

        temporaryConclusion.setRegistrationNumber(tempConclusionDto.getRegistrationNumber());
        temporaryConclusion.setCreationDate(utcFormatter.convertUTCToUTCPlus5(tempConclusionDto.getCreationDate()));
        temporaryConclusion.setUD(tempConclusionDto.getUdNumber());
        temporaryConclusion.setRegistrationDate(utcFormatter.convertUTCToUTCPlus5(tempConclusionDto.getRegistrationDate()));
        temporaryConclusion.setArticle(tempConclusionDto.getArticle());
        temporaryConclusion.setDecision(tempConclusionDto.getDecision());
        temporaryConclusion.setPlot(tempConclusionDto.getSummary());

        temporaryConclusion.setIINofCalled(tempConclusionDto.getCalledPersonIIN());
        temporaryConclusion.setFullNameOfCalled(tempConclusionDto.getCalledPersonFullName());
        temporaryConclusion.setJobTitleOfCalled(tempConclusionDto.getCalledPersonPosition());
        temporaryConclusion.setBINorIINOfCalled(tempConclusionDto.getCalledPersonBIN());

        temporaryConclusion.setJobPlace(tempConclusionDto.getWorkPlace());

        temporaryConclusion.setRegion(tempConclusionDto.getRegion());

        temporaryConclusion.setPlannedActions(tempConclusionDto.getPlannedInvestigativeActions());
        temporaryConclusion.setEventTime(utcFormatter.convertUTCToUTCPlus5(tempConclusionDto.getEventDateTime()));
        temporaryConclusion.setEventPlace(tempConclusionDto.getEventPlace());

        String[] name = tempConclusionDto.getInvestigator().split(" ");
        User investigator = userRepository.findByNameAndSecondName(name[1], name[2]).orElseThrow(() -> new UserNotFoundException("User not found."));
        temporaryConclusion.setInvestigator(investigator);

        Status status =  statusRepository.findByName(tempConclusionDto.getStatus());
        temporaryConclusion.setStatus(status);

        temporaryConclusion.setRelation(tempConclusionDto.getRelationToEvent());
        temporaryConclusion.setInvestigation(tempConclusionDto.getInvestigationTypes());
        temporaryConclusion.setBusiness(tempConclusionDto.isRelatesToBusiness());

        temporaryConclusion.setIINDefender(tempConclusionDto.getDefenseAttorneyIIN());
        temporaryConclusion.setFullNameOfDefender(tempConclusionDto.getDefenseAttorneyFullName());
        temporaryConclusion.setJustification(tempConclusionDto.getJustification());
        temporaryConclusion.setResult(tempConclusionDto.getResult());

        return temporaryConclusion;
    }

}
