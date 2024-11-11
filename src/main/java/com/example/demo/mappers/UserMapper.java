package com.example.demo.mappers;

import com.example.demo.dtos.requests.CreateUserRequest;
import com.example.demo.dtos.requests.LoginRequest;
import com.example.demo.dtos.responces.UserDto;
import com.example.demo.models.User;

import java.util.List;

public interface UserMapper {
    User fromRegisterToUser(CreateUserRequest createUserRequest);
    UserDto toUserDto(User user);
    List<UserDto> toDtoList(List<User> users);
}
