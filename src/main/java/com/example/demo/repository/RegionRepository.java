package com.example.demo.repository;

import com.example.demo.models.Region;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RegionRepository extends MongoRepository<Region,String> {
    Optional<Region> findRegionByName(String name);
}