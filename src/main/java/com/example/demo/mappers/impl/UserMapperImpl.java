package com.example.demo.mappers.impl;

import com.example.demo.dtos.requests.CreateUserRequest;
import com.example.demo.dtos.requests.LoginRequest;
import com.example.demo.dtos.responces.UserDto;
import com.example.demo.mappers.UserMapper;
import com.example.demo.models.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapperImpl implements UserMapper {
    @Override
    public User fromRegisterToUser(CreateUserRequest createUserRequest) {
        User user = new User();
        user.setEmail(createUserRequest.getEmail());
        user.setPassword(createUserRequest.getPassword());
        return user;

    }

    @Override
    public User fromLoginToUser(LoginRequest loginRequest) {
        User user = new User();
        user.setEmail(loginRequest.getEmail());
        user.setPassword(loginRequest.getPassword());

        return user;
    }

    @Override
    public UserDto toUserDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setEmail(user.getEmail());
        userDto.setName(user.getName());
        userDto.setProfileImage(user.getProfileImage());
        userDto.setRegistrationDate(user.getRegistrationDate());

        return userDto;
    }
}
