package com.example.demo.service.impl;

import com.example.demo.domain.Agreement;
import com.example.demo.repository.spec.AgreementRepository;
import com.example.demo.service.spec.AgreementService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AgreementServiceImpl implements AgreementService {
    private final AgreementRepository agreementRepository;

    @Override
    public List<Agreement> getAll() {
        return agreementRepository.findAll();
    }

    
}
