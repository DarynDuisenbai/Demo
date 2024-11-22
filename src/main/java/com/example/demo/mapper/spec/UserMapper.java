package com.example.demo.mapper.spec;

import com.example.demo.dto.request.user.CreateUserRequest;
import com.example.demo.dto.responce.UserDto;
import com.example.demo.domain.User;

import java.util.List;

public interface UserMapper {
    User fromRegisterToUser(CreateUserRequest createUserRequest);
    UserDto toUserDto(User user);
    List<UserDto> toDtoList(List<User> users);
}
