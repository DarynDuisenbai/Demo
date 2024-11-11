package com.example.demo.mappers.impl;

import com.example.demo.dtos.requests.CreateUserRequest;
import com.example.demo.dtos.requests.GetProfileRequest;
import com.example.demo.dtos.requests.LoginRequest;
import com.example.demo.dtos.responces.UserDto;
import com.example.demo.mappers.ConclusionMapper;
import com.example.demo.mappers.TempMapper;
import com.example.demo.mappers.UserMapper;
import com.example.demo.models.Department;
import com.example.demo.models.User;
import com.example.demo.repository.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMapperImpl implements UserMapper {
    private final ConclusionMapper conclusionMapper;
    private final TempMapper tempMapper;
    private final DepartmentRepository departmentRepository;
    @Override
    public User fromRegisterToUser(CreateUserRequest createUserRequest) {
        User user = new User();
        user.setEmail(createUserRequest.getEmail());
        user.setPassword(createUserRequest.getPassword());
        user.setName(createUserRequest.getName());
        user.setSecondName(createUserRequest.getSecondName());
        user.setIIN(createUserRequest.getIIN());

        Department department = departmentRepository.findDepartmentByNameAndRegion(
                createUserRequest.getDepartment(), createUserRequest.getRegion());

        user.setDepartment(department);

        return user;

    }
    @Override
    public UserDto toUserDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setEmail(user.getEmail());
        userDto.setPassword(user.getPassword());
        userDto.setName(user.getName());
        userDto.setSecondName(user.getSecondName());
        userDto.setProfileImage(user.getProfileImage());
        userDto.setRegistrationDate(user.getRegistrationDate());
        userDto.setRegion(user.getDepartment().getRegion());
        userDto.setDepartment(user.getDepartment().getName());

        userDto.setConclusions(conclusionMapper.toDtoList(user.getConclusions()));
        userDto.setTempConclusionDtos(tempMapper.toDtoList(user.getTemporaryConclusions()));
        userDto.setIIN(user.getIIN());
        return userDto;
    }
}
