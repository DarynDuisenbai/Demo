package com.example.demo.repository;

import com.example.demo.domain.User;

import java.util.List;

public interface FilterUserRepo {
    List<User> findByDepartment(String department);
    List<User> findByJob(String job);
    List<User> getBoss(User user);
    User findAnalystByDepartment(String department);
}
