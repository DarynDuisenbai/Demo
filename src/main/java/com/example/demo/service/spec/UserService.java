package com.example.demo.service.spec;

import com.example.demo.dto.request.user.*;
import com.example.demo.dto.responce.UserDto;
import com.example.demo.exception.*;
import com.example.demo.domain.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {
    UserDto login(LoginRequest loginRequest) throws InvalidLoginException, InvalidPasswordException;
    UserDto register(CreateUserRequest createUserRequest) throws InvalidLoginException, InvalidPasswordException, DuplicateUserException, InvalidIINFormat, InvalidUDFormat;
    boolean isPresent(String email);

    UserDto getProfile(String email) throws UserNotFoundException;
    void changePassword(ChangePasswordRequest changePasswordRequest) throws UserNotFoundException, InvalidPasswordException;
    void deleteAccount(DeleteAccountRequest deleteAccountRequest) throws UserNotFoundException;
    String forgotPassword(String email) throws UserNotFoundException;
    List<String> allNames();
    List<UserDto> getAllWithinDep(String department);
    List<UserDto> getAllWithinJob(String job);
    void promote(String IIN) throws UserNotFoundException, AnalystAlreadyExistsException;
    void editProfile(EditProfileRequest editProfileRequest) throws UserNotFoundException;
    List<UserDto> getAllUsers();
    User getAnalystOfDepartment(String department);
}
