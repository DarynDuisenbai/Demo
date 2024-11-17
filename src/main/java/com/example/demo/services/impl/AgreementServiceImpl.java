package com.example.demo.services.impl;

import com.example.demo.models.Agreement;
import com.example.demo.repository.AgreementRepository;
import com.example.demo.services.AgreementService;
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
