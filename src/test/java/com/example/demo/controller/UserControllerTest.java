package com.example.demo.controller;

import com.example.demo.dto.request.user.ChangePasswordRequest;
import com.example.demo.dto.request.user.ForgotPasswordRequest;
import com.example.demo.dto.responce.UserDto;
import com.example.demo.repository.spec.ActionRepository;
import com.example.demo.service.spec.UserService;
import com.example.demo.util.JwtTokenUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ActionRepository actionRepository;

    @MockBean
    private JwtTokenUtil jwtTokenUtil;

    @Test
    @WithMockUser
    void profile_shouldReturnUserProfile() throws Exception {
        UserDto userDto = new UserDto();
        userDto.setName("John Doe");

        Mockito.when(userService.getProfile("123456")).thenReturn(userDto);

        mockMvc.perform(get("/profile")
                        .param("IIN", "123456"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John Doe"));
    }
    @Test
    @WithMockUser
    void changePassword_shouldReturnSuccess() throws Exception {
        ChangePasswordRequest request = new ChangePasswordRequest();
        request.setOldPassword("oldPass");
        request.setNewPassword("newPass");

        mockMvc.perform(put("/password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string("Password changed successfully"));
    }
    @Test
    @WithMockUser
    void deleteAccount_shouldReturnSuccess() throws Exception {
        mockMvc.perform(delete("/delete")
                        .param("email", "test@example.com"))
                .andExpect(status().isOk())
                .andExpect(content().string("User has successfully deleted."));
    }
    @Test
    @WithMockUser
    void forgotPassword_shouldReturnSuccess() throws Exception {
        ForgotPasswordRequest request = new ForgotPasswordRequest();
        request.setEmail("test@example.com");

        mockMvc.perform(put("/reset-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string("Password changed successfully"));
    }

    @Test
    @WithMockUser
    void allNames_shouldReturnNames() throws Exception {
        Mockito.when(userService.allNames()).thenReturn(Arrays.asList("Alice", "Bob"));

        mockMvc.perform(get("/allNames"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").value("Alice"))
                .andExpect(jsonPath("$[1]").value("Bob"));
    }

    @Test
    @WithMockUser
    void depRelated_shouldReturnUsers() throws Exception {
        UserDto userDto = new UserDto();
        userDto.setName("John");

        Mockito.when(userService.getAllWithinDep("IT")).thenReturn(Collections.singletonList(userDto));

        mockMvc.perform(get("/depRelated")
                        .param("department", "IT"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("John"));
    }

    @Test
    @WithMockUser
    void jobRelated_shouldReturnUsers() throws Exception {
        UserDto userDto = new UserDto();
        userDto.setName("Jane");

        Mockito.when(userService.getAllWithinJob("Developer")).thenReturn(Collections.singletonList(userDto));

        mockMvc.perform(get("/jobRelated")
                        .param("jobTitle", "Developer"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Jane"));
    }
    @Test
    @WithMockUser
    void editProfile_shouldReturnSuccess() throws Exception {
        mockMvc.perform(put("/editProfile")
                        .param("IIN", "123456")
                        .param("name", "UpdatedName")
                        .param("surname", "UpdatedSurname"))
                .andExpect(status().isOk())
                .andExpect(content().string("Profile has successfully edited."));
    }

  /*  @Test
    @WithMockUser
    void getAllUsers_shouldReturnAllUsers() throws Exception {
        UserDto userDto = new UserDto();
        userDto.setName("User1");

        Mockito.when(userService.getAllUsers(0, 10)).thenReturn();

        mockMvc.perform(get("/allUsers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("User1"));
    }
*/
}
