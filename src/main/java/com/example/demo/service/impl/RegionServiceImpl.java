package com.example.demo.service.impl;

import com.example.demo.domain.Region;
import com.example.demo.repository.spec.RegionRepository;
import com.example.demo.service.spec.RegionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class RegionServiceImpl implements RegionService {

    private final RegionRepository regionRepository;

    @Override
    public List<String> allRegions() {
        return regionRepository.findAll().stream().map(Region::getName).toList();
    }


}
