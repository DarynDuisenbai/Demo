package com.example.demo.repository.spec;

import com.example.demo.domain.Region;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RegionRepository extends MongoRepository<Region,String>{
    Optional<Region> findRegionByName(String name);

}
