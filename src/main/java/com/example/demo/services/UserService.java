package com.example.demo.services;

import com.example.demo.dtos.requests.*;
import com.example.demo.dtos.responces.UserDto;
import com.example.demo.exceptions.DuplicateUserException;
import com.example.demo.exceptions.InvalidLoginException;
import com.example.demo.exceptions.InvalidPasswordException;
import com.example.demo.exceptions.UserNotFoundException;
import com.example.demo.models.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    UserDto login(LoginRequest loginRequest) throws InvalidLoginException, InvalidPasswordException;
    UserDto register(CreateUserRequest createUserRequest) throws InvalidLoginException, InvalidPasswordException, DuplicateUserException;
    boolean isPresent(String email);

    UserDto getProfile(GetProfileRequest getProfileRequest) throws UserNotFoundException;
    void changePassword(ChangePasswordRequest changePasswordRequest) throws UserNotFoundException, InvalidPasswordException;
    void deleteAccount(DeleteAccountRequest deleteAccountRequest) throws UserNotFoundException;
    void forgotPassword(ForgotPasswordRequest forgotPasswordRequest) throws UserNotFoundException;
}
