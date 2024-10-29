package com.example.demo.mappers;

import com.example.demo.dtos.requests.CreateUserRequest;
import com.example.demo.dtos.requests.LoginRequest;
import com.example.demo.dtos.responces.UserDto;
import com.example.demo.models.User;

public interface UserMapper {
    User fromRegisterToUser(CreateUserRequest createUserRequest);
    User fromLoginToUser(LoginRequest loginRequest);
    UserDto toUserDto(User user);
}
