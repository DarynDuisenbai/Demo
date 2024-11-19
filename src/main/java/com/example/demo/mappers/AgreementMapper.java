package com.example.demo.mappers;

import com.example.demo.dtos.responces.AgreementDto;
import com.example.demo.models.Agreement;

import java.util.List;

public interface AgreementMapper {
    AgreementDto toAgreementDto(Agreement agreement);
    Agreement fromDtoToAgreement(AgreementDto agreementDto);
    List<AgreementDto> toDtoList(List<Agreement> agreements);
}
