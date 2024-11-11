package com.example.demo.mappers.impl;

import com.example.demo.dtos.requests.CreateUserRequest;
import com.example.demo.dtos.requests.GetProfileRequest;
import com.example.demo.dtos.requests.LoginRequest;
import com.example.demo.dtos.responces.UserDto;
import com.example.demo.mappers.UserMapper;
import com.example.demo.models.Department;
import com.example.demo.models.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapperImpl implements UserMapper {
    @Override
    public User fromRegisterToUser(CreateUserRequest createUserRequest) {
        User user = new User();
        user.setEmail(createUserRequest.getEmail());
        user.setPassword(createUserRequest.getPassword());
        user.setName(createUserRequest.getName());
        user.setSecondName(createUserRequest.getSecondName());
        user.setIIN(createUserRequest.getIIN());

        Department department = new Department();
        department.setName(createUserRequest.getDepartment());
        department.setRegion(createUserRequest.getRegion());
        user.setDepartment(department);

        return user;

    }
    @Override
    public UserDto toUserDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setEmail(user.getEmail());
        userDto.setName(user.getName());
        userDto.setProfileImage(user.getProfileImage());
        userDto.setRegistrationDate(user.getRegistrationDate());
        userDto.setRegion(user.getDepartment().getRegion());
        userDto.setDepartment(user.getDepartment().getName());
        return userDto;
    }
}
