package com.example.demo.repository.spec;

import com.example.demo.domain.User;
import com.example.demo.repository.FilterUserRepo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String>, FilterUserRepo {
    Optional<User> findByEmail(String email);
    Optional<User> findByIIN(String iin);
    Optional<User> findByConclusionsRegNumbersContaining(String registrationNumber);
}
