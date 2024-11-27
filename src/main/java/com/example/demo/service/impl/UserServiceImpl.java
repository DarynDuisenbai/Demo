package com.example.demo.service.impl;

import com.example.demo.constant.JobConstants;
import com.example.demo.dto.request.user.*;
import com.example.demo.dto.responce.UserDto;
import com.example.demo.exception.*;
import com.example.demo.mapper.spec.UserMapper;
import com.example.demo.domain.JobTitle;
import com.example.demo.domain.User;
import com.example.demo.repository.spec.JobRepository;
import com.example.demo.repository.spec.UserRepository;
import com.example.demo.service.spec.UserService;
import com.example.demo.util.EmailSender;
import com.example.demo.util.UTCFormatter;
import com.example.demo.util.Validator;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);
    private final UserRepository userRepository;
    private final JobRepository jobRepository;
    private BCryptPasswordEncoder passwordEncoder;
    private final Validator validator;
    private final UserMapper userMapper;
    private final EmailSender emailSender;
    private final UTCFormatter utcFormatter;
    @Autowired
    public void setPasswordEncoder(BCryptPasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        LOGGER.warn("EMAIL IS " + email);
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
    public UserDto register(CreateUserRequest createUserRequest) throws InvalidLoginException, InvalidPasswordException, DuplicateUserException, InvalidIINFormat, InvalidUDFormat {
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
        if(!validator.isValidIIN(createUserRequest.getIIN())){
            LOGGER.warn("IIN is invalid...");
            throw new InvalidIINFormat("IIN code must be exactly 12 digits");
        }
        LOGGER.debug("Creating a new user...");
        User user = userMapper.fromRegisterToUser(createUserRequest);
        user.setPassword(passwordEncoder.encode(createUserRequest.getPassword()));
        user.setRegistrationDate(utcFormatter.convertUTCToUTCPlus5(LocalDateTime.now()));

        JobTitle jobTitle = jobRepository.findJobTitleByName(JobConstants.EMPLOYEE.getLabel());
        user.setJob(jobTitle);

        List<User> managers = userRepository.getBoss(user);
        if(managers == null) {
            user.setManagerIIN("");
        }else {
            user.setManagerIIN(managers.get(new Random().nextInt(managers.size())).getIIN());
        }
        userRepository.save(user);
        return userMapper.toUserDto(user);
    }

    @Override
    public boolean isPresent(String email) {
        return userRepository.findByEmail(email).orElse(null) != null;
    }

    @Override
    public UserDto getProfile(String IIN) throws UserNotFoundException {
        User user = userRepository.findByIIN(IIN).orElseThrow(() -> new UserNotFoundException("User with IIN: " + IIN + " not found."));
        LOGGER.debug("Retrieving a user...");
        return userMapper.toUserDto(user);
    }

    @Override
    public void changePassword(ChangePasswordRequest changePasswordRequest) throws UserNotFoundException, InvalidPasswordException {
        if (!isPresent(changePasswordRequest.getEmail())) {
            LOGGER.warn("User not found...");
            throw new UserNotFoundException("User with email: " + changePasswordRequest.getEmail() + " not found.");
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
            throw new UserNotFoundException("User with email: " + deleteAccountRequest.getEmail() + " not found.");
        }
        LOGGER.warn("Deleting a user...");
        User user = userRepository.findByEmail(deleteAccountRequest.getEmail()).get();
        userRepository.delete(user);
    }

    @Override
    public String forgotPassword(String email) throws UserNotFoundException {
        if(!isPresent(email)){
            LOGGER.warn("User not found...");
            throw new UserNotFoundException("User with email: " + email + " not found.");
        }
        try {
            emailSender.sendSetPasswordEmail(email);
        } catch (MessagingException e) {
            throw new RuntimeException("Unable to send set password email, please try again");
        }
        return "Please check your email to set new password to your account.";
    }

    @Override
    public List<String> allNames() {
        return userRepository.findAll().stream()
                .map(user -> user.getName() + " " + user.getSecondName()).toList();
    }

    @Override
    public List<UserDto> getAllWithinDep(String department) {
        return userMapper.toDtoList(
                userRepository.findByDepartment(department));
    }

    @Override
    public List<UserDto> getAllWithinJob(String job) {
        return userMapper.toDtoList(
                userRepository.findByJob(job));
    }

    @Override
    public void promote(String IIN) throws UserNotFoundException, AnalystAlreadyExistsException {
        User user = userRepository.findByIIN(IIN).orElseThrow(() -> new UserNotFoundException("User with IIN: " + IIN + " not found."));
        String job = user.getJob().getName();
        if(job.equals(JobConstants.EMPLOYEE.getLabel())){
            JobTitle curatorJob = jobRepository.findJobTitleByName(JobConstants.CURATOR.getLabel());
            user.setJob(curatorJob);

            List<User> managers = userRepository.getBoss(user);
            if(managers != null) {
                user.setManagerIIN(managers.get(new Random().nextInt(managers.size())).getIIN());
            }
        } else if (job.equals(JobConstants.CURATOR.getLabel())) {
            JobTitle specialistJob = jobRepository.findJobTitleByName(JobConstants.SPECIALIST.getLabel());
            user.setJob(specialistJob);

            List<User> managers = userRepository.getBoss(user);
            if(managers != null) {
                user.setManagerIIN(managers.get(new Random().nextInt(managers.size())).getIIN());
            }
        } else if(job.equals(JobConstants.SPECIALIST.getLabel())){
            JobTitle analystJob = jobRepository.findJobTitleByName(JobConstants.ANALYST.getLabel());
            User optAnalyst = userRepository.findAnalystByDepartment(user.getDepartment().getName());

            if(optAnalyst != null){
                throw new AnalystAlreadyExistsException("Analyst in this department already exists.");
            }
            user.setJob(analystJob);
            user.setManagerIIN("");
        }else if (job.equals(JobConstants.ANALYST.getLabel())) {
            JobTitle moderatorJob = jobRepository.findJobTitleByName(JobConstants.MODERATOR.getLabel());
            user.setJob(moderatorJob);
        }

        userRepository.save(user);
    }

    @Override
    public void editProfile(EditProfileRequest editProfileRequest) throws UserNotFoundException {
        User user = userRepository.findByIIN(editProfileRequest.getIIN()).orElseThrow(() -> new UserNotFoundException("User with IIN: " + editProfileRequest.getIIN() + " not found."));
        if (editProfileRequest.getName() != null) {
            user.setName(editProfileRequest.getName());
        }
        if (editProfileRequest.getSurname() != null) {
            user.setSecondName(editProfileRequest.getSurname());
        }

        userRepository.save(user);
    }

    @Override
    public List<UserDto> getAllUsers() {
        return userMapper.toDtoList(userRepository.findAll());
    }


    @Override
    public User getAnalystOfDepartment(String department) {
        return userRepository.findAnalystByDepartment(department);
    }


}

