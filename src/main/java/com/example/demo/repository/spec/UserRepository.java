package com.example.demo.repository.spec;

import com.example.demo.domain.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String>, FilterUserRepo{
    Optional<User> findByEmail(String email);
    Optional<User> findByIIN(String iin);
    Optional<User> findByNameAndSecondName(String name, String secondName);
}
