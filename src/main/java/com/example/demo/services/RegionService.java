package com.example.demo.services;

import com.example.demo.models.Region;

import java.util.List;
import java.util.Set;

public interface RegionService {
    List<String> allRegions();
    Set<String> allRegionsInDep(String dep);
}
