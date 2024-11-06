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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

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
        LOGGER.debug("Retrieving a user...");
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
            LOGGER.warn("Email is invalid...");
            throw new InvalidLoginException("Invalid email format.");
        }
        if (!validator.isValidPassword(loginRequest.getPassword())) {
            LOGGER.warn("Password is invalid...");
            throw new InvalidPasswordException("Password must contain at least 8 characters, including uppercase, lowercase, digit, and special character.");
        }
        User user = userRepository.findByEmail(loginRequest.getEmail()).orElse(null);
        if (user != null) {
            LOGGER.debug("Generating dto...");
            return userMapper.toUserDto(user);
        }
        return null;
    }

    @Override
    public UserDto register(CreateUserRequest createUserRequest) throws InvalidLoginException, InvalidPasswordException, DuplicateUserException {
        if (isPresent(createUserRequest.getEmail())) {
            LOGGER.warn("Email has been taken...");
            throw new DuplicateUserException("This email has been taken.");
        }
        if (!validator.isValidEmail(createUserRequest.getEmail())) {
            LOGGER.warn("Email is invalid...");
            throw new InvalidLoginException("Invalid email format.");
        }
        if (!validator.isValidPassword(createUserRequest.getPassword())) {
            LOGGER.warn("Password is invalid...");
            throw new InvalidPasswordException("Password must contain at least 8 characters, including uppercase, lowercase, digit, and special character.");
        }
        LOGGER.debug("Creating a new user...");
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
            LOGGER.warn("User not found...");
            throw new UserNotFoundException("User not found.");
        }
        LOGGER.debug("Retrieving a user...");
        User user = userRepository.findByEmail(getProfileRequest.getEmail()).get();
        return userMapper.toUserDto(user);
    }

    @Override
    public void changePassword(ChangePasswordRequest changePasswordRequest) throws UserNotFoundException, InvalidPasswordException {
        if (!isPresent(changePasswordRequest.getEmail())) {
            LOGGER.warn("User not found...");
            throw new UserNotFoundException("User not found.");
        }

        User user = userRepository.findByEmail(changePasswordRequest.getEmail()).get();
        if (!passwordEncoder.matches(changePasswordRequest.getOldPassword(), user.getPassword())) {
            LOGGER.warn("Old password incorrect...");
            throw new InvalidPasswordException("Old password is incorrect");
        }

        if (!validator.isValidPassword(changePasswordRequest.getNewPassword())) {
            LOGGER.warn("Password is invalid...");
            throw new InvalidPasswordException("New password must contain at least 8 characters, including uppercase, lowercase, digit, and special character.");
        }
        LOGGER.debug("Changing password...");
        user.setPassword(passwordEncoder.encode(changePasswordRequest.getNewPassword()));
        userRepository.save(user);
    }

    @Override
    public void deleteAccount(DeleteAccountRequest deleteAccountRequest) throws UserNotFoundException {
        if(!isPresent(deleteAccountRequest.getEmail())){
            LOGGER.warn("User not found...");
            throw new UserNotFoundException("User not found.");
        }
        LOGGER.warn("Deleting a user...");
        User user = userRepository.findByEmail(deleteAccountRequest.getEmail()).get();
        userRepository.delete(user);
    }

    @Override
    public void forgotPassword(ForgotPasswordRequest forgotPasswordRequest) throws UserNotFoundException {
        if(!isPresent(forgotPasswordRequest.getEmail())){
            LOGGER.warn("User not found...");
            throw new UserNotFoundException("User not found.");
        }


        String resetLink = "http://localhost:5002/reset-password?email=" + forgotPasswordRequest.getEmail();
        String emailContent = "<p>To reset your password, click the link below:</p>"
                + "<a href=\"" + resetLink + "\">Reset Password</a>";

        try{
            LOGGER.debug("Sending an email...");
            emailSender.sendEmail(forgotPasswordRequest.getEmail(), "Password Reset Request", emailContent);
        } catch (MessagingException e){
            LOGGER.warn("Error while sending email...");
            throw new RuntimeException("Failed to send email.");
        }
    }

    @Override
    public List<String> allNames() {
        return userRepository.findAll().stream()
                .map(user -> user.getName() + " " + user.getSecondName()).toList();
    }


}

