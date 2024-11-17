package com.example.demo.services;

import com.example.demo.dtos.requests.*;
import com.example.demo.dtos.responces.UserDto;
import com.example.demo.exceptions.*;
import com.example.demo.models.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {
    UserDto login(LoginRequest loginRequest) throws InvalidLoginException, InvalidPasswordException;
    UserDto register(CreateUserRequest createUserRequest) throws InvalidLoginException, InvalidPasswordException, DuplicateUserException, InvalidIINFormat, InvalidUDFormat;
    boolean isPresent(String email);

    UserDto getProfile(String email) throws UserNotFoundException;
    void changePassword(ChangePasswordRequest changePasswordRequest) throws UserNotFoundException, InvalidPasswordException;
    void deleteAccount(DeleteAccountRequest deleteAccountRequest) throws UserNotFoundException;
    void forgotPassword(ForgotPasswordRequest forgotPasswordRequest) throws UserNotFoundException;
    List<String> allNames();
    List<UserDto> getAllWithinDep(String department);
    List<UserDto> getAllWithinJob(String job);
    void promote(String IIN) throws UserNotFoundException;
    void editProfile(EditProfileRequest editProfileRequest) throws UserNotFoundException;
    List<UserDto> getAllUsers();

}
