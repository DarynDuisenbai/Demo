package com.example.demo.services.impl;

import com.example.demo.models.Region;
import com.example.demo.repository.RegionRepository;
import com.example.demo.services.RegionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RegionServiceImpl implements RegionService {

    private final RegionRepository regionRepository;

    @Override
    public List<String> allRegions() {
        return regionRepository.findAll().stream().map(Region::getName).toList();
    }

}
