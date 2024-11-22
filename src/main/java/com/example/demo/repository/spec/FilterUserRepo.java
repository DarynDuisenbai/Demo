package com.example.demo.repository.spec;

import com.example.demo.domain.User;

import java.util.List;

public interface FilterUserRepo {
    List<User> findByDepartment(String department);
    List<User> findByJob(String job);
    User findAnalystByDepartment(String department);
}
