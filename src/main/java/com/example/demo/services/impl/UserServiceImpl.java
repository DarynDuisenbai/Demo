package com.example.demo.services.impl;

import com.example.demo.dtos.requests.CreateUserRequest;
import com.example.demo.dtos.requests.GetProfileRequest;
import com.example.demo.dtos.requests.LoginRequest;
import com.example.demo.dtos.responces.UserDto;
import com.example.demo.exceptions.DuplicateUserException;
import com.example.demo.exceptions.InvalidLoginException;
import com.example.demo.exceptions.InvalidPasswordException;
import com.example.demo.mappers.UserMapper;
import com.example.demo.models.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.services.UserService;
import com.example.demo.utils.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private BCryptPasswordEncoder passwordEncoder;
    private Validator validator;
    private UserMapper userMapper;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, Validator validator, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.validator = validator;
        this.userMapper = userMapper;

    }

    @Autowired
    public void setPasswordEncoder(BCryptPasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found"));

        String role = user.getRole() != null ? user.getRole() : "USER";

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password(user.getPassword())
                .authorities(role)
                .build();
    }

    @Override
    public UserDto login(LoginRequest loginRequest) throws InvalidLoginException, InvalidPasswordException {
        if(!validator.isValidEmail(loginRequest.getEmail())){
            throw new InvalidLoginException("Invalid email format.");
        }
        if(!validator.isValidPassword(loginRequest.getPassword())){
            throw new InvalidPasswordException("Password must contain at least 8 characters, including uppercase, lowercase, digit, and special character.");
        }
        User user = userRepository.findByEmail(loginRequest.getEmail()).orElse(null);
        if(user != null){
            return userMapper.toUserDto(user);
        }
        return null;
    }

    @Override
    public UserDto register(CreateUserRequest createUserRequest) throws InvalidLoginException, InvalidPasswordException, DuplicateUserException {
        if(isPresent(createUserRequest.getEmail())){
            throw new DuplicateUserException("This email has been taken.");
        }
        if(!validator.isValidEmail(createUserRequest.getEmail())){
            throw new InvalidLoginException("Invalid email format.");
        }
        if(!validator.isValidPassword(createUserRequest.getPassword())){
            throw new InvalidPasswordException("Password must contain at least 8 characters, including uppercase, lowercase, digit, and special character.");
        }
        User user = userMapper.fromRegisterToUser(createUserRequest);
        user.setPassword(passwordEncoder.encode(createUserRequest.getPassword()));
        userRepository.save(user);
        return userMapper.toUserDto(user);
    }

    @Override
    public boolean isPresent(String email) {
        return userRepository.findByEmail(email).orElse(null) != null;
    }

    @Override
    public UserDto getProfile(GetProfileRequest getProfileRequest) {
        User user = userRepository.findByEmail(getProfileRequest.getEmail()).orElse(null);
        if(user == null){
            return null;
        }
        return userMapper.toUserDto(user);
    }
}
