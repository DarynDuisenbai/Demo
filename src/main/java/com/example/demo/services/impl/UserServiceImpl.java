package com.example.demo.services.impl;

import com.example.demo.dtos.requests.*;
import com.example.demo.dtos.responces.UserDto;
import com.example.demo.exceptions.DuplicateUserException;
import com.example.demo.exceptions.InvalidLoginException;
import com.example.demo.exceptions.InvalidPasswordException;
import com.example.demo.exceptions.UserNotFoundException;
import com.example.demo.mappers.UserMapper;
import com.example.demo.models.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.services.UserService;
import com.example.demo.utils.EmailSender;
import com.example.demo.utils.Validator;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private BCryptPasswordEncoder passwordEncoder;
    private final Validator validator;
    private final UserMapper userMapper;
    private final EmailSender emailSender;
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
        if (!validator.isValidEmail(loginRequest.getEmail())) {
            throw new InvalidLoginException("Invalid email format.");
        }
        if (!validator.isValidPassword(loginRequest.getPassword())) {
            throw new InvalidPasswordException("Password must contain at least 8 characters, including uppercase, lowercase, digit, and special character.");
        }
        User user = userRepository.findByEmail(loginRequest.getEmail()).orElse(null);
        if (user != null) {
            return userMapper.toUserDto(user);
        }
        return null;
    }

    @Override
    public UserDto register(CreateUserRequest createUserRequest) throws InvalidLoginException, InvalidPasswordException, DuplicateUserException {
        if (isPresent(createUserRequest.getEmail())) {
            throw new DuplicateUserException("This email has been taken.");
        }
        if (!validator.isValidEmail(createUserRequest.getEmail())) {
            throw new InvalidLoginException("Invalid email format.");
        }
        if (!validator.isValidPassword(createUserRequest.getPassword())) {
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
    public UserDto getProfile(GetProfileRequest getProfileRequest) throws UserNotFoundException {
        if (!isPresent(getProfileRequest.getEmail())) {
            throw new UserNotFoundException("User not found.");
        }
        User user = userRepository.findByEmail(getProfileRequest.getEmail()).get();
        return userMapper.toUserDto(user);
    }

    @Override
    public void changePassword(ChangePasswordRequest changePasswordRequest) throws UserNotFoundException, InvalidPasswordException {
        if (!isPresent(changePasswordRequest.getEmail())) {
            throw new UserNotFoundException("User not found.");
        }

        User user = userRepository.findByEmail(changePasswordRequest.getEmail()).get();
        if (!passwordEncoder.matches(changePasswordRequest.getOldPassword(), user.getPassword())) {
            throw new InvalidPasswordException("Old password is incorrect");
        }

        if (!validator.isValidPassword(changePasswordRequest.getNewPassword())) {
            throw new InvalidPasswordException("New password must contain at least 8 characters, including uppercase, lowercase, digit, and special character.");
        }

        user.setPassword(passwordEncoder.encode(changePasswordRequest.getNewPassword()));
        userRepository.save(user);
    }

    @Override
    public void deleteAccount(DeleteAccountRequest deleteAccountRequest) throws UserNotFoundException {
        if(!isPresent(deleteAccountRequest.getEmail())){
            throw new UserNotFoundException("User not found.");
        }
        User user = userRepository.findByEmail(deleteAccountRequest.getEmail()).get();
        userRepository.delete(user);
    }

    @Override
    public void forgotPassword(ForgotPasswordRequest forgotPasswordRequest) throws UserNotFoundException {
        if(!isPresent(forgotPasswordRequest.getEmail())){
            throw new UserNotFoundException("User not found.");
        }

        String resetLink = "http://localhost:5002/reset-password?email=" + forgotPasswordRequest.getEmail();
        String emailContent = "<p>To reset your password, click the link below:</p>"
                + "<a href=\"" + resetLink + "\">Reset Password</a>";

        try{
            emailSender.sendEmail(forgotPasswordRequest.getEmail(), "Password Reset Request", emailContent);
        } catch (MessagingException e){
            throw new RuntimeException("Failed to send email.");
        }
    }
}

