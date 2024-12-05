package com.example.demo.service.impl;

import com.example.demo.domain.Business;
import com.example.demo.repository.spec.BusinessRepository;
import com.example.demo.service.spec.BusinessService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BusinessServiceImpl implements BusinessService {
    private final BusinessRepository businessRepository;
    @Override
    public List<String> allBusinesses() {
        return businessRepository.findAll().stream().map(Business::getName).toList();
    }
}
