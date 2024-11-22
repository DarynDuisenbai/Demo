package com.example.demo.mapper.spec;

import com.example.demo.dto.responce.AgreementDto;
import com.example.demo.domain.Agreement;

import java.util.List;

public interface AgreementMapper {
    AgreementDto toAgreementDto(Agreement agreement);
    Agreement fromDtoToAgreement(AgreementDto agreementDto);
    List<AgreementDto> toDtoList(List<Agreement> agreements);
}
