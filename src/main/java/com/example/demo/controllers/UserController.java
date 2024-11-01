package com.example.demo.controllers;

import com.example.demo.dtos.requests.GetProfileRequest;
import com.example.demo.dtos.responces.UserDto;
import com.example.demo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/profile")
    public ResponseEntity<UserDto> profile(@RequestBody GetProfileRequest getProfileRequest){
        UserDto userProf = userService.getProfile(getProfileRequest);
        return ResponseEntity.status(HttpStatus.OK).body(userProf);
    }

}
