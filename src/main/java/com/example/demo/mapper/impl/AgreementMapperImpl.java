package com.example.demo.mapper.impl;

import com.example.demo.dto.responce.AgreementDto;
import com.example.demo.mapper.spec.AgreementMapper;
import com.example.demo.domain.Agreement;
import com.example.demo.domain.Status;
import com.example.demo.repository.spec.StatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class AgreementMapperImpl implements AgreementMapper {

    private final StatusRepository statusRepository;


    @Override
    public AgreementDto toAgreementDto(Agreement agreement) {
        AgreementDto agreementDto = new AgreementDto();
        agreementDto.setRegistrationNumber(agreement.getRegNumber());
        agreementDto.setJobTitle(agreement.getJobTitle());
        agreementDto.setFullName(agreement.getFullName());
        agreementDto.setStatus(agreement.getStatus().getName());
        agreementDto.setReason(agreement.getReason());
        agreementDto.setDate(agreement.getDate());

        return agreementDto;
    }

    @Override
    public Agreement fromDtoToAgreement(AgreementDto agreementDto) {
        Agreement agreement = new Agreement();
        agreement.setRegNumber(agreementDto.getRegistrationNumber());
        agreement.setJobTitle(agreementDto.getJobTitle());
        agreement.setFullName(agreementDto.getFullName());

        Status status = statusRepository.findByName(agreementDto.getStatus());
        agreement.setStatus(status);

        agreement.setReason(agreementDto.getReason());
        agreement.setDate(agreementDto.getDate());

        return agreement;
    }

    @Override
    public List<AgreementDto> toDtoList(List<Agreement> agreements) {
        List<AgreementDto> agreementDtos = new ArrayList<>();
        for(Agreement agreement : agreements){
            agreementDtos.add(toAgreementDto(agreement));
        }

        return agreementDtos;
    }
}
