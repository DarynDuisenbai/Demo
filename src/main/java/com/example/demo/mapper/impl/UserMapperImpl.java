package com.example.demo.mapper.impl;

import com.example.demo.dto.request.user.CreateUserRequest;
import com.example.demo.dto.responce.UserDto;
import com.example.demo.mapper.spec.AgreementMapper;
import com.example.demo.mapper.spec.ConclusionMapper;
import com.example.demo.mapper.spec.TempMapper;
import com.example.demo.mapper.spec.UserMapper;
import com.example.demo.domain.Department;
import com.example.demo.domain.JobTitle;
import com.example.demo.domain.User;
import com.example.demo.repository.spec.DepartmentRepository;
import com.example.demo.repository.spec.JobRepository;
import com.example.demo.repository.spec.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class UserMapperImpl implements UserMapper {
    private final DepartmentRepository departmentRepository;
    private final UserRepository userRepository;
    private final JobRepository jobRepository;
    private final AgreementMapper agreementMapper;

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
        userDto.setDepartment(user.getDepartment());
        userDto.setManager(userDto.getManager());

        userDto.setConclusions(user.getConclusionsRegNumbers());
        userDto.setTempConclusionDtos(user.getTemporaryConclusionsRegNumbers());
        userDto.setReceivedConclusionDtos(user.getReceivedConclusionsRegNumbers());
        userDto.setAgreementDtos(agreementMapper.toDtoList(user.getAgreements()));
        userDto.setIIN(user.getIIN());

        JobTitle jobTitle = jobRepository.findJobTitleByName(user.getJob().getName());
        userDto.setJobTitle(jobTitle);
        return userDto;
    }

    @Override
    public List<UserDto> toDtoList(List<User> users) {
        List<UserDto> dtos = new ArrayList<>();
        for(User user : users){
            dtos.add(toUserDto(user));
        }

        return dtos;
    }
}
